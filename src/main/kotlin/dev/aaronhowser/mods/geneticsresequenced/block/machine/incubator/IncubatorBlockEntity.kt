package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.handler.WrappedHandler
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.DupeCellRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.ItemStackHandler


class IncubatorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.INCUBATOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "incubator"

    override val energyMaximum: Int = 50_000
    override val energyTransferMaximum: Int = 500

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return IncubatorMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return ModBlocks.INCUBATOR.get().name
    }

    override val amountOfItemSlots: Int = 5
    override val itemHandler: ItemStackHandler = object : ItemStackHandler(amountOfItemSlots) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                TOP_SLOT_INDEX -> AbstractIncubatorRecipe.isValidTopIngredient(level!!, stack)

                LEFT_BOTTLE_SLOT_INDEX,
                MIDDLE_BOTTLE_SLOT_INDEX,
                RIGHT_BOTTLE_SLOT_INDEX -> AbstractIncubatorRecipe.isValidBottomIngredient(level!!, stack)

                OVERCLOCKER_SLOT_INDEX -> stack.item == ModItems.OVERCLOCKER.get()

                else -> false
            }
        }
    }

    override val upItemHandler: WrappedHandler = topSlotHandler(itemHandler)
    override val downItemHandler: WrappedHandler = bottomSlotsHandler(itemHandler)
    override val backItemHandler: WrappedHandler = bottomSlotsHandler(itemHandler)
    override val frontItemHandler: WrappedHandler = bottomSlotsHandler(itemHandler)
    override val rightItemHandler: WrappedHandler = bottomSlotsHandler(itemHandler)
    override val leftItemHandler: WrappedHandler = bottomSlotsHandler(itemHandler)

    private val data = object : ContainerData {

        private var remainingTicks = 0

        override fun get(pIndex: Int): Int {
            return when (pIndex) {
                REMAINING_TICKS_INDEX -> remainingTicks
                else -> 0
            }
        }

        override fun set(pIndex: Int, pValue: Int) {
            when (pIndex) {
                REMAINING_TICKS_INDEX -> remainingTicks = pValue
            }
        }

        override fun getCount(): Int {
            return 1
        }
    }

    private var ticksRemaining: Int
        get() = data.get(REMAINING_TICKS_INDEX)
        set(value) {
            data.set(REMAINING_TICKS_INDEX, value)
        }
    private val isBrewing: Boolean
        get() = ticksRemaining > 0

    private val ticksRemainingNbtKey = "$machineName.ticksRemaining"

    override val baseEnergyCostPerTick = 10

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        pTag.putInt(ticksRemainingNbtKey, ticksRemaining)

        super.saveAdditional(pTag, pRegistries)
    }

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        ticksRemaining = pTag.getInt(ticksRemainingNbtKey)

        super.loadAdditional(pTag, pRegistries)
    }

    override val amountOfOverclockers: Int
        get() = itemHandler.getStackInSlot(OVERCLOCKER_SLOT_INDEX).count

    override fun tick() {

        val hasRecipe = hasRecipe()

        if (!isBrewing && hasRecipe) {
            ticksRemaining = ticksPerBrew
        } else if (!hasRecipe) {
            ticksRemaining = 0
            return
        }

        energyStorage.extractEnergy(energyCostPerTick(), false)
        ticksRemaining -= 1 + amountOfOverclockers

        if (ticksRemaining <= 0) {
            craftItem()
            return
        }

    }

    override fun craftItem() {
        val topStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)

        val bottleSlots = listOf(
            LEFT_BOTTLE_SLOT_INDEX,
            MIDDLE_BOTTLE_SLOT_INDEX,
            RIGHT_BOTTLE_SLOT_INDEX
        )

        var onlyDupeCellRecipes = true

        for (slotIndex in bottleSlots) {
            val bottomStack = itemHandler.getStackInSlot(slotIndex)

            val recipeInput = IncubatorRecipeInput(topStack, bottomStack, isHighTemp = true)
            val incubatorRecipe = AbstractIncubatorRecipe.getIncubatorRecipe(level!!, recipeInput)

            if (incubatorRecipe != null) {
                if (incubatorRecipe !is DupeCellRecipe) onlyDupeCellRecipes = false

                val output = incubatorRecipe.assemble(recipeInput, level!!.registryAccess())

                if (!output.isEmpty) {
                    itemHandler.setStackInSlot(slotIndex, output)
                }
            } else {
                val potionBrewing = level!!.potionBrewing()
                val hasMix = potionBrewing.hasMix(bottomStack, topStack)

                if (hasMix) {
                    onlyDupeCellRecipes = false

                    val output = potionBrewing.mix(topStack, bottomStack)

                    if (!output.isEmpty) {
                        itemHandler.setStackInSlot(slotIndex, output)
                    }
                }
            }
        }

        if (!onlyDupeCellRecipes) topStack.shrink(1)
        setChanged()
    }

    override fun hasRecipe(): Boolean {
        if (!hasEnoughEnergy()) return false

        val topStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)
        if (topStack.isEmpty) return false

        val bottomStacks = listOf(
            itemHandler.getStackInSlot(LEFT_BOTTLE_SLOT_INDEX),
            itemHandler.getStackInSlot(MIDDLE_BOTTLE_SLOT_INDEX),
            itemHandler.getStackInSlot(RIGHT_BOTTLE_SLOT_INDEX)
        )

        return bottomStacks.any { bottomStack ->
            if (bottomStack.isEmpty) return@any false

            val input = IncubatorRecipeInput(topStack, bottomStack, isHighTemp = true)
            AbstractIncubatorRecipe.hasIncubatorRecipe(level!!, input)
        }
    }

    companion object {

        fun topSlotHandler(itemHandler: IItemHandlerModifiable): WrappedHandler =
            WrappedHandler(
                itemHandler,
                canExtract = { slotId -> slotId == OUTPUT_SLOT_INDEX },
                canInsert = { slotId, stack -> slotId == TOP_SLOT_INDEX }
            )


        fun bottomSlotsHandler(itemHandler: IItemHandlerModifiable): WrappedHandler =
            WrappedHandler(
                itemHandler,
                canExtract = { slotId -> slotId == LEFT_BOTTLE_SLOT_INDEX || slotId == MIDDLE_BOTTLE_SLOT_INDEX || slotId == RIGHT_BOTTLE_SLOT_INDEX },
                canInsert = { slotId, stack -> slotId == LEFT_BOTTLE_SLOT_INDEX || slotId == MIDDLE_BOTTLE_SLOT_INDEX || slotId == RIGHT_BOTTLE_SLOT_INDEX }
            )

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: IncubatorBlockEntity
        ) {
            if (level.isClientSide) return
            blockEntity.tick()
        }

        const val SIMPLE_CONTAINER_SIZE = 1
        const val REMAINING_TICKS_INDEX = 0

        const val TOP_SLOT_INDEX = 0
        const val LEFT_BOTTLE_SLOT_INDEX = 1
        const val MIDDLE_BOTTLE_SLOT_INDEX = 2
        const val RIGHT_BOTTLE_SLOT_INDEX = 3
        const val OVERCLOCKER_SLOT_INDEX = 4

        val ticksPerBrew: Int
            get() = ServerConfig.incubatorTicksPerBrew.get()
    }

}