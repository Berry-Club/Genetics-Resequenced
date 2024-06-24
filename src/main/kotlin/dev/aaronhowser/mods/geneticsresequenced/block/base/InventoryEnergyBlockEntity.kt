package dev.aaronhowser.mods.geneticsresequenced.block.base

import dev.aaronhowser.mods.geneticsresequenced.block.base.handler.ModEnergyStorage
import dev.aaronhowser.mods.geneticsresequenced.block.base.handler.WrappedHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemStackHandler

abstract class InventoryEnergyBlockEntity(
    blockEntityType: BlockEntityType<*>,
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(
    blockEntityType,
    pPos,
    pBlockState
) {

    abstract val machineName: String

    protected val inventoryNbtKey: String
        get() = "${machineName}.inventory"
    protected val energyNbtKey: String
        get() = "${machineName}.energy"

    abstract val energyMaximum: Int
    abstract val energyTransferMaximum: Int

    abstract val amountOfItemSlots: Int
    abstract val itemHandler: ItemStackHandler

    protected abstract val upItemHandler: WrappedHandler
    protected abstract val downItemHandler: WrappedHandler
    protected abstract val backItemHandler: WrappedHandler
    protected abstract val frontItemHandler: WrappedHandler
    protected abstract val rightItemHandler: WrappedHandler
    protected abstract val leftItemHandler: WrappedHandler

    private val directionWrappedHandlerMap: Map<Direction, WrappedHandler>
        get() = mapOf(
            Direction.DOWN to downItemHandler,
            Direction.UP to upItemHandler,
            Direction.NORTH to backItemHandler,
            Direction.SOUTH to frontItemHandler,
            Direction.EAST to rightItemHandler,
            Direction.WEST to leftItemHandler
        )

    val energyStorage: ModEnergyStorage by lazy {
        object : ModEnergyStorage(energyMaximum, energyTransferMaximum) {
            override fun onEnergyChanged() {
                setChanged()
            }
        }
    }

    open fun getItemCapability(side: Direction?): IItemHandler {
        if (side == null) return itemHandler

        if (side == Direction.UP) return upItemHandler
        if (side == Direction.DOWN) return downItemHandler

        val directionFacing = this.blockState.getValue(HorizontalDirectionalBlock.FACING)

        return when (directionFacing) {
            Direction.NORTH -> directionWrappedHandlerMap[side.opposite]!!
            Direction.SOUTH -> directionWrappedHandlerMap[side]!!
            Direction.EAST -> directionWrappedHandlerMap[side.clockWise]!!
            Direction.WEST -> directionWrappedHandlerMap[side.counterClockWise]!!

            else -> directionWrappedHandlerMap[side]!!
        }

    }

    /**
     * @throws IllegalStateException if called on the server side
     */
    fun setClientEnergy(energy: Int) {
        if (level?.isClientSide == false) throw IllegalStateException("setClientEnergy called on server side")
        this.energyStorage.setEnergy(energy)
    }

    override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag {
        return saveWithoutMetadata(pRegistries)
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    override fun setChanged() {
        super.setChanged()
        level?.sendBlockUpdated(blockPos, blockState, blockState, 3)
    }

    fun dropDrops() {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        Containers.dropContents(this.level!!, this.blockPos, inventory)
    }

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        pTag.put(inventoryNbtKey, itemHandler.serializeNBT(pRegistries))
        pTag.putInt(energyNbtKey, energyStorage.energyStored)
        super.saveAdditional(pTag, pRegistries)
    }

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        itemHandler.deserializeNBT(pRegistries, pTag.getCompound(inventoryNbtKey))
        energyStorage.setEnergy(pTag.getInt(energyNbtKey))
        super.loadAdditional(pTag, pRegistries)

    }

}