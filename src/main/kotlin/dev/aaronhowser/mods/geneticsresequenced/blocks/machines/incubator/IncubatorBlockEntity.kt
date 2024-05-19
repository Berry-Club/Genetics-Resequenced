package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

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
    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu? {
        return IncubatorMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.incubator")
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

    private val ticksRemainingNbtKey = "$machineName.ticksRemaining"

    override fun saveAdditional(pTag: CompoundTag) {
        pTag.putInt(ticksRemainingNbtKey, ticksRemaining)

        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        ticksRemaining = pTag.getInt(ticksRemainingNbtKey)

        super.load(pTag)
    }


    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: IncubatorBlockEntity
        ) {
            if (level.isClientSide) return
        }

        private const val REMAINING_TICKS_INDEX = 0
    }

}