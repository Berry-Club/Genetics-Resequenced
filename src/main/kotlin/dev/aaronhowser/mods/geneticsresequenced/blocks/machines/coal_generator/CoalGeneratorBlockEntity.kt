package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.InventoryAndEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.energy.IEnergyStorage
import kotlin.math.min

@Suppress("UNUSED_PARAMETER")
class CoalGeneratorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : InventoryAndEnergyBlockEntity(
    ModBlockEntities.COAL_GENERATOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val inventoryNbtKey: String = "coal_generator.inventory"
    override val energyNbtKey = "coal_generator.energy"
    override val energyMaximum: Int = ServerConfig.coalGeneratorEnergyCapacity.get()
    override val energyTransferMaximum: Int = ServerConfig.coalGeneratorEnergyTransferRate.get()

    override val menuType: Class<out MachineMenu> = CoalGeneratorMenu::class.java
    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return CoalGeneratorMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.coal_generator")
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

            val fuelReplacedItem = inputItem.craftingRemainingItem

            blockEntity.maxBurnTime = fuel
            blockEntity.burnTimeRemaining = fuel
            blockEntity.itemHandler.extractItem(INPUT_SLOT, 1, false)

            if (!fuelReplacedItem.isEmpty) {
                blockEntity.itemHandler.insertItem(INPUT_SLOT, fuelReplacedItem, false)
            }

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
            val maxEnergy = blockEntity.energyStorage.maxEnergyStored
            val currentEnergy = blockEntity.energyStorage.energyStored
            return currentEnergy + energyPerTick <= maxEnergy
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

        // How many values are stored in the container data
        // Here it's two: remaining ticks and max burn time
        const val SIMPLE_CONTAINER_SIZE = 2

        const val ITEMSTACK_HANDLER_SIZE = 1
        const val INPUT_SLOT = 0

        val energyPerTick: Int
            get() = ServerConfig.coalGeneratorEnergyPerTick.get()

        const val REMAINING_TICKS_INDEX = 0
        const val MAX_BURN_TIME_INDEX = 1
    }

}