package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.geneticsresequenced.block.CoalGeneratorBlock
import dev.aaronhowser.mods.geneticsresequenced.block.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block_old.machine.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class CoalGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : InventoryEnergyBlockEntity(ModBlockEntityTypes.COAL_GENERATOR.get(), pos, blockState) {

	override val maxEnergy: Int = ServerConfig.CONFIG.coalGeneratorEnergyCapacity.get()
	override val energyTransferRate: Int = ServerConfig.CONFIG.coalGeneratorEnergyTransferRate.get()
	override val containerSize: Int = CONTAINER_SIZE

	private var burnTimeRemaining: Int = 0
		set(value) {
			field = value.coerceAtLeast(0)
			setChanged()
		}

	private var maxBurnTime: Int = 0
		set(value) {
			field = value.coerceAtLeast(0)
			setChanged()
		}

	private val containerData = object : ContainerData {
		override fun set(index: Int, value: Int) {
			when (index) {
				REMAINING_TICKS_INDEX -> burnTimeRemaining = value
				MAX_BURN_TIME_INDEX -> maxBurnTime = value
			}
		}

		override fun get(index: Int): Int {
			return when (index) {
				REMAINING_TICKS_INDEX -> burnTimeRemaining
				MAX_BURN_TIME_INDEX -> maxBurnTime
				else -> -1
			}
		}

		override fun getCount(): Int = CONTAINER_DATA_SIZE
	}

	private fun serverTick() {
		if (hasRoomForEnergy()) {
			if (burnTimeRemaining > 0) {
				generateEnergy()
			} else {
				tryStartBurning()
			}
		}
	}

	private fun tryStartBurning() {
		val level = this.level ?: return

		val inputItem = container.getItem(INPUT_INDEX)
		val fuelTime = inputItem.getBurnTime(RecipeType.SMELTING)

		if (fuelTime <= 0) return

		val newState = blockState.setValue(CoalGeneratorBlock.BURNING, true)
		level.setBlockAndUpdate(blockPos, newState)

		val fuelReplacedItem = inputItem.craftingRemainingItem

		maxBurnTime = fuelTime
		burnTimeRemaining = fuelTime

		invWrapper.extractItem(INPUT_INDEX, 1, false)

		if (!fuelReplacedItem.isEmpty && invWrapper.getStackInSlot(INPUT_INDEX).isEmpty) {
			invWrapper.insertItem(INPUT_INDEX, fuelReplacedItem, false)
		}
	}

	private fun generateEnergy() {
		val level = this.level ?: return
		energyStorage.receiveEnergy(getEnergyPerTick(level), false)

		burnTimeRemaining--
	}

	private fun hasRoomForEnergy(): Boolean {
		return energyStorage.energyStored < energyStorage.maxEnergyStored
	}

	override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
		return CoalGeneratorMenu(pContainerId, pPlayerInventory, container, containerData)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(BURN_TIME_REMAINING_NBT, burnTimeRemaining)
		tag.putInt(MAX_BURN_TIME_NBT, maxBurnTime)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		burnTimeRemaining = tag.getInt(BURN_TIME_REMAINING_NBT)
		maxBurnTime = tag.getInt(MAX_BURN_TIME_NBT)
	}

	companion object {
		const val BURN_TIME_REMAINING_NBT = "BurnTimeRemaining"
		const val MAX_BURN_TIME_NBT = "MaxBurnTime"

		const val CONTAINER_SIZE = 1
		const val INPUT_INDEX = 0

		const val CONTAINER_DATA_SIZE = 2
		const val REMAINING_TICKS_INDEX = 0
		const val MAX_BURN_TIME_INDEX = 1

		fun getEnergyPerTick(level: Level): Int {
			return ServerConfig.CONFIG.coalGeneratorEnergyPerTick.get()
		}
	}
}