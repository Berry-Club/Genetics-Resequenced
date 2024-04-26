package dev.aaronhowser.mods.geneticsresequenced.blocks.base

import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.EnergySyncPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ModEnergyStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.Containers
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler

/**
 * Base class for all crafting machines.
 * Note that this is only for basic crafting machines, ie, those that turn one thing into one other thing.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class CraftingMachineBlockEntity(
    pType: BlockEntityType<*>,
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(
    pType,
    pPos,
    pBlockState
), MenuProvider {

    abstract val inventoryNbtKey: String


    companion object {
        const val SIMPLE_CONTAINER_SIZE = 2
        const val ITEMSTACK_HANDLER_SIZE = 3

        const val INPUT_SLOT_INDEX = 0
        const val OUTPUT_SLOT_INDEX = 1
        const val OVERCLOCK_SLOT_INDEX = 2
    }

    abstract val energyNbtKey: String
    abstract val energyMaximum: Int
    abstract val energyTransferMaximum: Int
    abstract val energyCostPerTick: Int

    abstract val itemHandler: ItemStackHandler

    protected var lazyItemHandler: LazyOptional<IItemHandler> = LazyOptional.empty()

    val amountOfOverclockers: Int
        get() = itemHandler.getStackInSlot(OVERCLOCK_SLOT_INDEX).count

    protected val energyStorage: ModEnergyStorage by lazy {
        object : ModEnergyStorage(energyMaximum, energyTransferMaximum) {
            override fun onEnergyChanged() {
                setChanged()

                ModPacketHandler.messageAllPlayers(EnergySyncPacket(energy, blockPos))
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

    protected fun getItemCapability(side: Direction?): LazyOptional<IItemHandler> {
        return lazyItemHandler.cast()
    }

    protected fun getEnergyCapability(side: Direction?): LazyOptional<IEnergyStorage> {
        return lazyEnergyStorage.cast()
    }

    fun getEnergyStorage(): IEnergyStorage = energyStorage

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

    fun drops() {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        Containers.dropContents(this.level!!, this.blockPos, inventory)
    }

    protected var progress = 0
    protected var maxProgress = 20 * 4
    protected fun resetProgress() {
        progress = 0
    }

    // Client only
    fun setEnergy(energy: Int) {
        this.energyStorage.setEnergy(energy)
    }

    protected val containerData = object : ContainerData {

        private val PROGRESS_INDEX = 0
        private val MAX_PROGRESS_INDEX = 1

        override fun get(pIndex: Int): Int {
            return when (pIndex) {
                PROGRESS_INDEX -> progress
                MAX_PROGRESS_INDEX -> maxProgress
                else -> 0
            }
        }

        override fun set(pIndex: Int, pValue: Int) {
            when (pIndex) {
                PROGRESS_INDEX -> progress = pValue
                MAX_PROGRESS_INDEX -> maxProgress = pValue
            }
        }

        override fun getCount(): Int {
            return SIMPLE_CONTAINER_SIZE
        }
    }

}