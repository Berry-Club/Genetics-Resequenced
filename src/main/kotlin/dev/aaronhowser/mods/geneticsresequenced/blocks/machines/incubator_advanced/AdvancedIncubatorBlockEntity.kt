package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.WrappedHandler
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator.IncubatorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.GmoRecipe
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.ItemStackHandler
import kotlin.random.Random

class AdvancedIncubatorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.ADVANCED_INCUBATOR.get(),
    pPos,
    pBlockState
) {

    override val machineName: String = "advanced_incubator"

    override val energyMaximum: Int = 50_000
    override val energyTransferMaximum: Int = 500

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return AdvancedIncubatorMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.advanced_incubator")
    }

    override val amountOfItemSlots: Int = 6
    override val itemHandler: ItemStackHandler = object : ItemStackHandler(amountOfItemSlots) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                TOP_SLOT_INDEX -> BrewingRecipeRegistry.isValidIngredient(stack)

                LEFT_BOTTLE_SLOT_INDEX,
                MIDDLE_BOTTLE_SLOT_INDEX,
                RIGHT_BOTTLE_SLOT_INDEX -> BrewingRecipeRegistry.isValidInput(stack)

                CHORUS_SLOT_INDEX -> stack.`is`(Items.CHORUS_FRUIT)

                OVERCLOCKER_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER.get())

                else -> false
            }
        }
    }

    private val chorusHandler = LazyOptional.of {
        WrappedHandler(
            itemHandler,
            canExtract = { slot -> slot == OUTPUT_SLOT_INDEX },
            canInsert = { slot, stack -> slot == CHORUS_SLOT_INDEX }
        )
    }

    override val upItemHandler: LazyOptional<WrappedHandler> = IncubatorBlockEntity.ingredientHandler(itemHandler)
    override val downItemHandler: LazyOptional<WrappedHandler> = IncubatorBlockEntity.outputHandler(itemHandler)
    override val backItemHandler: LazyOptional<WrappedHandler> = chorusHandler
    override val frontItemHandler: LazyOptional<WrappedHandler> = chorusHandler
    override val rightItemHandler: LazyOptional<WrappedHandler> = IncubatorBlockEntity.inputHandler(itemHandler)
    override val leftItemHandler: LazyOptional<WrappedHandler> = IncubatorBlockEntity.inputHandler(itemHandler)

    private val data = object : ContainerData {

        private var remainingTicks = 0
        private var isHighTemperature = 0

        override fun get(pIndex: Int): Int {
            return when (pIndex) {
                REMAINING_TICKS_INDEX -> remainingTicks
                IS_HIGH_TEMPERATURE_INDEX -> isHighTemperature
                else -> 0
            }
        }

        override fun set(pIndex: Int, pValue: Int) {
            when (pIndex) {
                REMAINING_TICKS_INDEX -> remainingTicks = pValue
                IS_HIGH_TEMPERATURE_INDEX -> isHighTemperature = pValue
            }
            setChanged()
        }

        override fun getCount(): Int {
            return SIMPLE_CONTAINER_SIZE
        }
    }

    private val ticksRemainingNbtKey = "$machineName.ticksRemaining"
    private var ticksRemaining: Int
        get() = data.get(REMAINING_TICKS_INDEX)
        set(value) {
            data.set(REMAINING_TICKS_INDEX, value)
        }

    private val isHighTemperatureNbtKey = "$machineName.isHighTemperature"
    private var isHighTemperature: Boolean
        get() = data.get(IS_HIGH_TEMPERATURE_INDEX) == 1
        set(value) {
            data.set(IS_HIGH_TEMPERATURE_INDEX, if (value) 1 else 0)
        }

    private val isBrewing: Boolean
        get() = ticksRemaining > 0

    override val energyCostPerTick = 10

    override fun saveAdditional(pTag: CompoundTag) {
        pTag.putInt(ticksRemainingNbtKey, ticksRemaining)
        pTag.putBoolean(isHighTemperatureNbtKey, isHighTemperature)

        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        ticksRemaining = pTag.getInt(ticksRemainingNbtKey)
        isHighTemperature = pTag.getBoolean(isHighTemperatureNbtKey)

        super.load(pTag)
    }


    override val amountOfOverclockers: Int
        get() = itemHandler.getStackInSlot(OVERCLOCKER_SLOT_INDEX).count

    private var subticks = 0
    override fun tick() {

        if (!isBrewing && hasRecipe()) {
            ticksRemaining = IncubatorBlockEntity.ticksPerBrew
        } else if (!hasRecipe()) {
            ticksRemaining = 0
            return
        }

        if (isHighTemperature) {
            energyStorage.extractEnergy(energyCostPerTick, false)
            ticksRemaining -= 1 + amountOfOverclockers
        } else {
            val amountOfOverclockers = amountOfOverclockers
            subticks += 1 + amountOfOverclockers

            val subticksOverMax = subticks - lowTempTickFactor
            if (subticksOverMax >= 0) {
                subticks = subticksOverMax
                energyStorage.extractEnergy(energyCostPerTick, false)
                ticksRemaining -= 1
            }
        }

        if (ticksRemaining <= 0) {
            craftItem()
            return
        }
    }

    /**
     * Higher is better
     */
    private fun getMutationModifier(): Float {
        val chorusCount = itemHandler.getStackInSlot(CHORUS_SLOT_INDEX).count
        return if (chorusCount > 0) {
            0f
        } else {
            - amountOfOverclockers * MUTATION_CHANCE_PER_OVERCLOCKER
        }
    }

    override fun craftItem() {

        val topStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)

        val bottleSlots = listOf(
            LEFT_BOTTLE_SLOT_INDEX,
            MIDDLE_BOTTLE_SLOT_INDEX,
            RIGHT_BOTTLE_SLOT_INDEX
        )

        for (slotIndex in bottleSlots) {
            val bottleStack = itemHandler.getStackInSlot(slotIndex)

            var output = BrewingRecipeRegistry.getOutput(bottleStack, topStack)

            if (output.item == ModItems.GMO_CELL.get() && !isHighTemperature) {
                val gmoRecipes = ModPotions.allRecipes.filterIsInstance<GmoRecipe>()

                val thisRecipe = gmoRecipes.find {
                    it.ingredientItem == topStack.item
                            && it.entityType == EntityDnaItem.getEntityType(bottleStack)
                            && it.requiredPotion == PotionUtils.getPotion(bottleStack)
                } ?: continue

                val chanceModifier = getMutationModifier()
                val chance = thisRecipe.geneChance + chanceModifier
                val nextFloat = Random.nextFloat()

                if (nextFloat <= chance) {
                    output = thisRecipe.getSuccess()
                }

                if (chanceModifier != 0f) {
                    val chorusStack = itemHandler.getStackInSlot(CHORUS_SLOT_INDEX)
                    chorusStack.shrink(1)
                }
            }

            if (!output.isEmpty) {
                itemHandler.setStackInSlot(slotIndex, output)
            }
        }

        topStack.shrink(1)
        setChanged()
    }

    override fun hasRecipe(): Boolean {
        if (energyStorage.energyStored < energyCostPerTick) return false

        val topSlotStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)
        val bottleStacks =
            listOf(
                itemHandler.getStackInSlot(LEFT_BOTTLE_SLOT_INDEX),
                itemHandler.getStackInSlot(MIDDLE_BOTTLE_SLOT_INDEX),
                itemHandler.getStackInSlot(RIGHT_BOTTLE_SLOT_INDEX)
            )

        return bottleStacks.any {
            BrewingRecipeRegistry.hasOutput(it, topSlotStack)
        }

    }

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: AdvancedIncubatorBlockEntity
        ) {
            if (level.isClientSide) return
            blockEntity.tick()
        }

        const val SIMPLE_CONTAINER_SIZE = 2
        const val REMAINING_TICKS_INDEX = 0
        const val IS_HIGH_TEMPERATURE_INDEX = 1

        const val TOP_SLOT_INDEX = 0
        const val LEFT_BOTTLE_SLOT_INDEX = 1
        const val MIDDLE_BOTTLE_SLOT_INDEX = 2
        const val RIGHT_BOTTLE_SLOT_INDEX = 3
        const val CHORUS_SLOT_INDEX = 4
        const val OVERCLOCKER_SLOT_INDEX = 5

        val lowTempTickFactor: Int
            get() = ServerConfig.incubatorLowTempTickFactor.get()

        const val MUTATION_CHANCE_PER_OVERCLOCKER = -0.1f
    }

}