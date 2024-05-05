package dev.aaronhowser.mods.geneticsresequenced.blocks.base

import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator.CoalGeneratorBlockEntity.Companion.ITEMSTACK_HANDLER_SIZE
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.util.ModEnergyStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler

@Suppress("MemberVisibilityCanBePrivate")
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

    open val itemHandler: ItemStackHandler by lazy {
        object : ItemStackHandler(ITEMSTACK_HANDLER_SIZE) {
            override fun onContentsChanged(slot: Int) {
                setChanged()
            }

            override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
                return true
            }
        }
    }

    protected var lazyItemHandler: LazyOptional<IItemHandler> = LazyOptional.empty()

    val energyStorage: ModEnergyStorage by lazy {
        object : ModEnergyStorage(energyMaximum, energyTransferMaximum) {
            override fun onEnergyChanged() {
                setChanged()
            }
        }
    }

    protected var lazyEnergyStorage: LazyOptional<IEnergyStorage> = LazyOptional.empty()

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return when (cap) {
            ForgeCapabilities.ITEM_HANDLER -> getItemCapability(side).cast()

            ForgeCapabilities.ENERGY -> getEnergyCapability(side).cast()

            else -> super.getCapability(cap, side)
        }
    }

    //TODO: Make it so that you can only extract from the bottom
    protected fun getItemCapability(side: Direction?): LazyOptional<IItemHandler> {
        return lazyItemHandler.cast()
    }

    //TODO: Make it so you can't pull out of machines, and can't insert into generators
    protected fun getEnergyCapability(side: Direction?): LazyOptional<IEnergyStorage> {
        return lazyEnergyStorage.cast()
    }

    abstract val menuType: Class<out MachineMenu>

    /**
     * @throws IllegalStateException if called on the server side
     */
    fun setClientEnergy(energy: Int) {
        if (level?.isClientSide == false) throw IllegalStateException("setClientEnergy called on server side")
        this.energyStorage.setEnergy(energy)
    }

    override fun getUpdateTag(): CompoundTag = saveWithoutMetadata()

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

    override fun onLoad() {
        super.onLoad()
        lazyItemHandler = LazyOptional.of { itemHandler }
        lazyEnergyStorage = LazyOptional.of { energyStorage }
    }

    override fun invalidateCaps() {
        super.invalidateCaps()
        lazyItemHandler.invalidate()
        lazyEnergyStorage.invalidate()
    }

    override fun saveAdditional(pTag: CompoundTag) {
        pTag.put(inventoryNbtKey, itemHandler.serializeNBT())
        pTag.putInt(energyNbtKey, energyStorage.energyStored)
        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        super.load(pTag)

        itemHandler.deserializeNBT(pTag.getCompound(inventoryNbtKey))
        energyStorage.setEnergy(pTag.getInt(energyNbtKey))
    }

}