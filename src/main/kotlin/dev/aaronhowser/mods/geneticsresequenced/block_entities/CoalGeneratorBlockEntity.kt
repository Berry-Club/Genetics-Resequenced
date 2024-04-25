package dev.aaronhowser.mods.geneticsresequenced.block_entities

import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.EnergySyncPacket
import dev.aaronhowser.mods.geneticsresequenced.screens.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.util.ModEnergyStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler
import kotlin.math.min

@Suppress("UNUSED_PARAMETER")
class CoalGeneratorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(
    ModBlockEntities.COAL_GENERATOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    private val itemHandler: ItemStackHandler = object : ItemStackHandler(ITEMSTACK_HANDLER_SIZE) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return true
        }
    }

    private var lazyItemHandler = LazyOptional.empty<IItemHandler>()

    private val energyStorage: ModEnergyStorage = object : ModEnergyStorage(CAPACITY, MAX_TRANSFER) {
        override fun onEnergyChanged() {
            setChanged()

            ModPacketHandler.messageAllPlayers(EnergySyncPacket(energy, blockPos))
        }
    }

    private var lazyEnergyStorage = LazyOptional.empty<IEnergyStorage>()

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return CoalGeneratorMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.coal_generator")
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return when (cap) {
            ForgeCapabilities.ITEM_HANDLER -> getItemCapability(side).cast()

            ForgeCapabilities.ENERGY -> getEnergyCapability(side).cast()

            else -> super.getCapability(cap, side)
        }
    }

    private fun getItemCapability(side: Direction?): LazyOptional<IItemHandler> {
        return lazyItemHandler.cast()
    }

    private fun getEnergyCapability(side: Direction?): LazyOptional<IEnergyStorage> {
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
        pTag.put(INVENTORY_NBT_KEY, itemHandler.serializeNBT())
        pTag.putInt(ENERGY_NBT_KEY, energyStorage.energyStored)
        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        super.load(pTag)
        itemHandler.deserializeNBT(pTag.getCompound(INVENTORY_NBT_KEY))
        energyStorage.setEnergy(pTag.getInt(ENERGY_NBT_KEY))
    }

    fun drops() {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        Containers.dropContents(this.level!!, this.blockPos, inventory)
    }

    // Client only
    fun setEnergy(energy: Int) {
        this.energyStorage.setEnergy(energy)
    }

    private val data = object : ContainerData {
        private var _totalTicksToBurn = 0
            set(value) {
                field = value
                _ticksToBurnRemaining = _ticksToBurnRemaining.coerceAtMost(value)
            }
        private var _ticksToBurnRemaining = 0
            set(value) {
                field = value.coerceIn(0, _totalTicksToBurn)
            }

        override fun get(pIndex: Int): Int {
            return when (pIndex) {
                REMAINING_TICKS_INDEX -> _ticksToBurnRemaining
                MAX_BURN_TIME_INDEX -> _totalTicksToBurn
                else -> 0
            }
        }

        override fun set(pIndex: Int, pValue: Int) {
            when (pIndex) {
                REMAINING_TICKS_INDEX -> _ticksToBurnRemaining = pValue
                MAX_BURN_TIME_INDEX -> _totalTicksToBurn = pValue
            }
        }

        override fun getCount(): Int {
            return SIMPLE_CONTAINER_SIZE
        }
    }

    private var maxBurnTime: Int
        get() = data.get(MAX_BURN_TIME_INDEX)
        set(value) {
            data.set(MAX_BURN_TIME_INDEX, value)
        }

    private var burnTimeRemaining: Int
        get() = data.get(REMAINING_TICKS_INDEX)
        set(value) {
            data.set(REMAINING_TICKS_INDEX, value)
        }

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: CoalGeneratorBlockEntity
        ) {
            if (level.isClientSide) return

            pushEnergyToAdjacent(blockEntity)

            if (!hasRoomForEnergy(blockEntity)) return

            if (isBurning(blockEntity)) {
                generateEnergy(blockEntity)
            } else {
                tryToStartBurning(blockEntity)
            }
        }

        private fun tryToStartBurning(blockEntity: CoalGeneratorBlockEntity) {

            val inputItem = blockEntity.itemHandler.getStackInSlot(INPUT_SLOT)
            val fuel = ForgeHooks.getBurnTime(inputItem, RecipeType.SMELTING)

            if (fuel <= 0) {
                blockEntity.maxBurnTime = 0
                return
            }

            blockEntity.maxBurnTime = fuel
            blockEntity.burnTimeRemaining = fuel
            blockEntity.itemHandler.extractItem(INPUT_SLOT, 1, false)
            blockEntity.setChanged()
        }

        private fun generateEnergy(blockEntity: CoalGeneratorBlockEntity) {
            blockEntity.energyStorage.receiveEnergy(energyPerTick, false)

            blockEntity.burnTimeRemaining -= 1
            blockEntity.setChanged()
        }

        private fun isBurning(blockEntity: CoalGeneratorBlockEntity): Boolean {
            return blockEntity.burnTimeRemaining > 0
        }

        private fun hasRoomForEnergy(blockEntity: CoalGeneratorBlockEntity): Boolean {
            return blockEntity.energyStorage.energyStored + energyPerTick <= blockEntity.energyStorage.maxEnergyStored
        }

        fun pushEnergyToAdjacent(blockEntity: CoalGeneratorBlockEntity) {
            val energyStorage = blockEntity.energyStorage
            val blockPos = blockEntity.blockPos
            val level = blockEntity.level

            if (energyStorage.energyStored <= 0) return
            for (direction in Direction.values()) {
                val neighborPos = blockPos.offset(direction.normal)
                val neighborEntity = level?.getBlockEntity(neighborPos) ?: continue

                val neighborEnergy: IEnergyStorage =
                    neighborEntity.getCapability(ForgeCapabilities.ENERGY, direction.opposite).orElse(null) ?: continue
                if (!neighborEnergy.canReceive()) continue

                val energyToTransfer = min(
                    neighborEnergy.receiveEnergy(MAX_TRANSFER, true),
                    neighborEnergy.maxEnergyStored - neighborEnergy.energyStored
                )

                if (energyToTransfer <= 0) continue
                neighborEnergy.receiveEnergy(
                    energyStorage.extractEnergy(energyToTransfer, false),
                    false
                )
            }
        }

        private const val INVENTORY_NBT_KEY = "coal_generator.inventory"

        // How many values are stored in the container data
        // Here it's two: remaining ticks and max burn time
        const val SIMPLE_CONTAINER_SIZE = 2

        const val ITEMSTACK_HANDLER_SIZE = 1
        const val INPUT_SLOT = 0

        private const val ENERGY_NBT_KEY = "coal_generator.energy"

        private const val CAPACITY = 60_000
        private const val MAX_TRANSFER = 256

        val energyPerTick: Int
            get() = ServerConfig.coalGeneratorEnergyPerTick.get()

        const val REMAINING_TICKS_INDEX = 0
        const val MAX_BURN_TIME_INDEX = 1
    }

}