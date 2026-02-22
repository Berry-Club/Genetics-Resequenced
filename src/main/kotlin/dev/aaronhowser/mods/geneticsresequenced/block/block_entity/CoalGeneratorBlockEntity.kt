package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.geneticsresequenced.block.CoalGeneratorBlock
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.container_data.CraftingContainerData
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.ForgeCapabilities
import kotlin.jvm.optionals.getOrNull

class CoalGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : MachineBlockEntity(ModBlockEntityTypes.COAL_GENERATOR.get(), pos, blockState) {

	override val maxEnergy: Int = ServerConfig.CONFIG.coalGeneratorEnergyCapacity.get()
	override val energyTransferRate: Int = ServerConfig.CONFIG.coalGeneratorEnergyTransferRate.get()

	override val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, CONTAINER_SIZE)

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

	override val containerData: ContainerData = CraftingContainerData(
		energyStorage,
		{ burnTimeRemaining },
		{ maxBurnTime }
	)

	override fun serverTick() {
		exportEnergy()

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

		val inputItem = container.getItem(INPUT_SLOT_INDEX)
		val fuelTime = ForgeHooks.getBurnTime(inputItem, RecipeType.SMELTING)

		if (fuelTime <= 0) return

		val newState = blockState.setValue(CoalGeneratorBlock.BURNING, true)
		level.setBlockAndUpdate(blockPos, newState)

		val fuelReplacedItem = inputItem.craftingRemainingItem

		maxBurnTime = fuelTime
		burnTimeRemaining = fuelTime

		itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)

		if (fuelReplacedItem.isNotEmpty() && itemHandler.getStackInSlot(INPUT_SLOT_INDEX).isEmpty) {
			itemHandler.insertItem(INPUT_SLOT_INDEX, fuelReplacedItem, false)
		}
	}

	private fun generateEnergy() {
		energyStorage.receiveEnergy(getEnergyPerTick(), false)
		burnTimeRemaining--
	}

	private fun hasRoomForEnergy(): Boolean {
		return energyStorage.energyStored < energyStorage.maxEnergyStored
	}

	private fun exportEnergy() {
		val level = this.level ?: return

		if (energyStorage.energyStored <= 0) return

		for (direction in Direction.entries) {
			val neighborPos = blockPos.relative(direction)
			val neighborBlockEntity = level.getBlockEntity(neighborPos) ?: continue
			val neighborEnergy = neighborBlockEntity
				.getCapability(ForgeCapabilities.ENERGY, direction.opposite)
				.resolve()
				.getOrNull()
				?: continue

			if (!neighborEnergy.canReceive()) continue

			val maxEnergyToSend = minOf(
				energyTransferRate,
				energyStorage.energyStored
			)

			val energyToTransfer = neighborEnergy.receiveEnergy(maxEnergyToSend, false)
			energyStorage.extractEnergy(energyToTransfer, false)
		}
	}

	override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
		return CoalGeneratorMenu(pContainerId, pPlayerInventory, container, containerData)
	}

	override fun saveAdditional(pTag: CompoundTag) {
		super.saveAdditional(pTag)

		pTag.putInt(BURN_TIME_REMAINING_NBT, burnTimeRemaining)
		pTag.putInt(MAX_BURN_TIME_NBT, maxBurnTime)
	}

	override fun load(pTag: CompoundTag) {
		super.load(pTag)

		burnTimeRemaining = pTag.getInt(BURN_TIME_REMAINING_NBT)
		maxBurnTime = pTag.getInt(MAX_BURN_TIME_NBT)
	}

	companion object {
		const val BURN_TIME_REMAINING_NBT = "BurnTimeRemaining"
		const val MAX_BURN_TIME_NBT = "MaxBurnTime"

		const val CONTAINER_SIZE = 1
		const val INPUT_SLOT_INDEX = 0

		const val CONTAINER_DATA_SIZE = 2
		const val REMAINING_TICKS_INDEX = 0
		const val MAX_BURN_TIME_INDEX = 1

		fun getEnergyPerTick(): Int = ServerConfig.CONFIG.coalGeneratorEnergyPerTick.get()
	}
}