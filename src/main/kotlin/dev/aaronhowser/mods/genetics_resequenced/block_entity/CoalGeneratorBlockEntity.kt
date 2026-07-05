package dev.aaronhowser.mods.genetics_resequenced.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.genetics_resequenced.block.CoalGeneratorBlock
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.MachineBlockEntity
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.SidedMachineItemHandler
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.container_data.CraftingContainerData
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.menu.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandlerUtil
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction

class CoalGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : MachineBlockEntity(ModBlockEntityTypes.COAL_GENERATOR.get(), pos, blockState) {

	override val maxEnergy: Int = ServerConfig.CONFIG.coalGeneratorEnergyCapacity.get()
	override val energyTransferRate: Int = ServerConfig.CONFIG.coalGeneratorEnergyTransferRate.get()

	override val container: ImprovedSimpleContainer = object : ImprovedSimpleContainer(this, CONTAINER_SIZE) {
		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return slot == INPUT_SLOT_INDEX && getBurnTime(stack) > 0
		}
	}

	private val fuelHandler: ResourceHandler<ItemResource> by lazy {
		SidedMachineItemHandler(
			itemHandler,
			INPUT_SLOT_INDEX,
			canInsert = ::canAutomateInsert,
			canExtract = { _, stack -> getBurnTime(stack) <= 0 }
		)
	}

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
		val fuelTime = getBurnTime(inputItem)

		if (fuelTime <= 0) return

		val newState = blockState.setValue(CoalGeneratorBlock.BURNING, true)
		level.setBlockAndUpdate(blockPos, newState)

		val fuelReplacedItem = inputItem.item.getCraftingRemainder(inputItem)?.create() ?: ItemStack.EMPTY

		maxBurnTime = fuelTime
		burnTimeRemaining = fuelTime

		itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)

		if (fuelReplacedItem.isNotEmpty() && itemHandler.getStackInSlot(INPUT_SLOT_INDEX).isEmpty) {
			itemHandler.insertItem(INPUT_SLOT_INDEX, fuelReplacedItem, false)
		}
	}

	private fun generateEnergy() {
		insertEnergy(getEnergyPerTick())
		burnTimeRemaining--
	}

	private fun hasRoomForEnergy(): Boolean {
		return energyStorage.getAmountAsInt() < energyStorage.getCapacityAsInt()
	}

	private fun exportEnergy() {
		val level = this.level ?: return

		if (energyStorage.getAmountAsInt() <= 0) return

		for (direction in Direction.entries) {
			val neighborPos = blockPos.relative(direction)
			val neighborEnergy = level.getCapability(
				Capabilities.Energy.BLOCK,
				neighborPos,
				direction.getOpposite()
			) ?: continue

			val maxEnergyToSend = minOf(
				energyTransferRate,
				energyStorage.getAmountAsInt()
			)

			Transaction.openRoot().use { transaction ->
				val moved = EnergyHandlerUtil.move(energyStorage, neighborEnergy, maxEnergyToSend, transaction)
				if (moved > 0) {
					transaction.commit()
				}
			}
		}
	}

	private fun getBurnTime(stack: ItemStack): Int {
		val level = this.level ?: return 0
		return stack.getBurnTime(RecipeType.SMELTING, level.fuelValues())
	}

	override fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource> {
		return fuelHandler
	}

	override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
		return CoalGeneratorMenu(pContainerId, pPlayerInventory, container, containerData)
	}

	override fun saveAdditional(output: ValueOutput) {
		super.saveAdditional(output)

		output.putInt(BURN_TIME_REMAINING_NBT, burnTimeRemaining)
		output.putInt(MAX_BURN_TIME_NBT, maxBurnTime)
	}

	override fun loadAdditional(input: ValueInput) {
		super.loadAdditional(input)

		burnTimeRemaining = input.getIntOr(BURN_TIME_REMAINING_NBT, 0)
		maxBurnTime = input.getIntOr(MAX_BURN_TIME_NBT, 0)
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
