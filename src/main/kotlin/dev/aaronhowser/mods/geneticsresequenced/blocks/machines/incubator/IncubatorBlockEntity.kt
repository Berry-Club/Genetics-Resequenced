package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import net.minecraftforge.items.ItemStackHandler

class IncubatorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : InventoryEnergyBlockEntity(
    ModBlockEntities.INCUBATOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "coal_generator"

    override val energyMaximum: Int = 50_000
    override val energyTransferMaximum: Int = 500

    override val menuType: Class<out MachineMenu> = IncubatorMenu::class.java
    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return IncubatorMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.incubator")
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

    override fun saveAdditional(pTag: CompoundTag) {
        pTag.putInt(ticksRemainingNbtKey, ticksRemaining)

        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        ticksRemaining = pTag.getInt(ticksRemainingNbtKey)

        super.load(pTag)
    }

    private fun tick() {

        if (!isBrewing && canBrew()) {
            ticksRemaining = TICKS_PER
        } else if (!canBrew()) {
            ticksRemaining = 0
            return
        }

        energyStorage.extractEnergy(ENERGY_COST_PER_TICK, false)
        ticksRemaining--

        if (ticksRemaining <= 0) {
            finishBrew()
            return
        }

    }

    private fun finishBrew() {

        val topStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)
        val topPotion = PotionUtils.getPotion(topStack)

        val bottleSlots = listOf(
            LEFT_BOTTLE_SLOT_INDEX,
            MIDDLE_BOTTLE_SLOT_INDEX,
            RIGHT_BOTTLE_SLOT_INDEX
        )

        for (slotIndex in bottleSlots) {
            val bottleStack = itemHandler.getStackInSlot(slotIndex)
            val output = BrewingRecipeRegistry.getOutput(bottleStack, topStack)

            if (!output.isEmpty) {
                itemHandler.setStackInSlot(slotIndex, output)
            }
        }

        topStack.shrink(1)
        setChanged()
    }

    private fun canBrew(): Boolean {
        if (energyStorage.energyStored < ENERGY_COST_PER_TICK) return false

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
        const val CHORUS_SLOT_INDEX = 4
        const val OVERCLOCKER_SLOT_INDEX = 5

        private const val ENERGY_COST_PER_TICK = 10
        const val TICKS_PER = 20 * 5
    }

}