package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.WrappedHandler
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.ItemStackHandler
import kotlin.math.min

@Suppress("UNUSED_PARAMETER")
class CoalGeneratorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : InventoryEnergyBlockEntity(
    ModBlockEntities.COAL_GENERATOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "coal_generator"

    override val energyMaximum: Int = ServerConfig.coalGeneratorEnergyCapacity.get()
    override val energyTransferMaximum: Int = ServerConfig.coalGeneratorEnergyTransferRate.get()

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return CoalGeneratorMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.coal_generator")
    }


    override val amountOfItemSlots: Int = 1
    override val itemHandler: ItemStackHandler = object : ItemStackHandler(amountOfItemSlots) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0
        }
    }

    private val allSideHandler = LazyOptional.of {
        WrappedHandler(
            itemHandler,
            canExtract = { false },
            canInsert = { slotId, stack -> slotId == INPUT_SLOT }
        )
    }

    override val upItemHandler: LazyOptional<WrappedHandler> = allSideHandler
    override val downItemHandler: LazyOptional<WrappedHandler> = allSideHandler
    override val backItemHandler: LazyOptional<WrappedHandler> = allSideHandler
    override val frontItemHandler: LazyOptional<WrappedHandler> = allSideHandler
    override val rightItemHandler: LazyOptional<WrappedHandler> = allSideHandler
    override val leftItemHandler: LazyOptional<WrappedHandler> = allSideHandler

    private val data = object : ContainerData {
        private var _totalTicksToBurn = 0
            set(value) {
                field = value
                _ticksToBurnRemaining = _ticksToBurnRemaining.coerceAtMost(_totalTicksToBurn)
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

    private val burnTicksLeftNbtKey = "$machineName.burn_ticks_left"
    private val maxBurnTicksNbtKey = "$machineName.max_burn_ticks"

    override fun saveAdditional(pTag: CompoundTag) {
        pTag.putInt(burnTicksLeftNbtKey, burnTimeRemaining)
        pTag.putInt(maxBurnTicksNbtKey, maxBurnTime)

        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        maxBurnTime = pTag.getInt(maxBurnTicksNbtKey)
        burnTimeRemaining = pTag.getInt(burnTicksLeftNbtKey)

        super.load(pTag)
    }

    private var maxBurnTime: Int
        get() = data.get(MAX_BURN_TIME_INDEX)
        set(value) {
            data.set(MAX_BURN_TIME_INDEX, value)
            setChanged()
        }

    private var burnTimeRemaining: Int
        get() {
            val t = data.get(REMAINING_TICKS_INDEX)
            return t
        }
        set(value) {
            data.set(REMAINING_TICKS_INDEX, value)
            setChanged()
        }

    private fun tryToStartBurning() {

        val inputItem = itemHandler.getStackInSlot(INPUT_SLOT)
        val fuel = ForgeHooks.getBurnTime(inputItem, RecipeType.SMELTING)

        if (fuel <= 0) {
            stopBurning()
        } else {
            startBurning(inputItem, fuel)
        }

        setChanged()
    }

    private fun stopBurning() {
        maxBurnTime = 0
        burnTimeRemaining = 0
        val state = blockState.setValue(CoalGeneratorBlock.BURNING, false)
        level?.setBlock(blockPos, state, 3)
    }

    private fun startBurning(inputItem: ItemStack, fuel: Int) {
        val state = blockState.setValue(CoalGeneratorBlock.BURNING, true)
        level?.setBlock(blockPos, state, 3)

        //FIXME: Dont work
        val fuelReplacedItem = inputItem.craftingRemainingItem

        maxBurnTime = fuel
        burnTimeRemaining = fuel
        itemHandler.extractItem(INPUT_SLOT, 1, false)

        if (!fuelReplacedItem.isEmpty) {
            itemHandler.insertItem(INPUT_SLOT, fuelReplacedItem, false)
        }
    }

    private fun generateEnergy() {
        energyStorage.receiveEnergy(energyPerTick, false)

        burnTimeRemaining -= 1
        setChanged()
    }

    val isBurning: Boolean
        get() = burnTimeRemaining > 0

    private fun hasRoomForEnergy(): Boolean {
        val maxEnergy = energyStorage.maxEnergyStored
        val currentEnergy = energyStorage.energyStored
        return currentEnergy + energyPerTick <= maxEnergy
    }

    private fun pushEnergyToAdjacent() {

        if (energyStorage.energyStored <= 0) return
        for (direction in Direction.values()) {
            val neighborPos = blockPos.offset(direction.normal)
            val neighborEntity = level?.getBlockEntity(neighborPos) ?: continue

            val neighborEnergy: IEnergyStorage =
                neighborEntity.getCapability(ForgeCapabilities.ENERGY, direction.opposite).orElse(null) ?: continue
            if (!neighborEnergy.canReceive()) continue

            val energyToTransfer = min(
                neighborEnergy.receiveEnergy(ServerConfig.coalGeneratorEnergyTransferRate.get(), true),
                neighborEnergy.maxEnergyStored - neighborEnergy.energyStored
            )

            if (energyToTransfer <= 0) continue
            neighborEnergy.receiveEnergy(
                energyStorage.extractEnergy(energyToTransfer, false),
                false
            )
        }
    }

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: CoalGeneratorBlockEntity
        ) {
            if (level.isClientSide) return

            blockEntity.apply {
                pushEnergyToAdjacent()

                if (!hasRoomForEnergy()) return

                if (isBurning) {
                    generateEnergy()
                } else {
                    tryToStartBurning()
                }
            }
        }


        // How many values are stored in the container data
        // Here it's two: remaining ticks and max burn time
        const val SIMPLE_CONTAINER_SIZE = 2

        const val INPUT_SLOT = 0

        val energyPerTick: Int
            get() = ServerConfig.coalGeneratorEnergyPerTick.get()

        const val REMAINING_TICKS_INDEX = 0
        const val MAX_BURN_TIME_INDEX = 1
    }

}