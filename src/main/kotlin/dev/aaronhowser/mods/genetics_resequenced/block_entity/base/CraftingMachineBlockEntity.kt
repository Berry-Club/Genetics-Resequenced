package dev.aaronhowser.mods.genetics_resequenced.block_entity.base

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.genetics_resequenced.block.base.MachineBlock
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.container_data.CraftingContainerData
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.function.IntSupplier

abstract class CraftingMachineBlockEntity(
	blockEntityType: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : MachineBlockEntity(blockEntityType, pos, blockState) {

	abstract val baseEnergyCostPerTick: IntSupplier

	override val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, DEFAULT_INVENTORY_SIZE)

	open fun getEnergyCostPerTick(): Int {
		val extraPerOverclocker = Mth.ceil(baseEnergyCostPerTick.asInt * 0.25f)
		val totalExtraCost = (extraPerOverclocker * getAmountOfOverclocks())

		return baseEnergyCostPerTick.asInt + totalExtraCost
	}

	open fun getAmountOfOverclocks(): Int {
		return container.getItem(OVERCLOCK_SLOT_INDEX).count
	}

	protected fun hasEnoughEnergy(): Boolean {
		return energyStorage.amountAsInt >= getEnergyCostPerTick()
	}

	protected fun drainEnergy(): Boolean {
		val cost = getEnergyCostPerTick()
		if (energyStorage.amountAsInt < cost) return false
		extractEnergy(cost)
		return true
	}

	protected var currentProgress: Int = 0
		set(value) {
			if (field != value) {
				field = value
				setChanged()
			}
		}

	protected var maxProgress: Int = DEFAULT_MAX_PROGRESS
		set(value) {
			if (field != value) {
				field = value
				setChanged()
			}
		}

	override val containerData: ContainerData by lazy {
		CraftingContainerData(energyStorage, ::currentProgress, ::maxProgress)
	}

	protected abstract fun hasRecipe(): Boolean
	protected abstract fun craftItem()

	override fun serverTick() {
		if (!hasEnoughEnergy()) return
		if (!hasRecipe()) {
			currentProgress = 0
			return
		}

		drainEnergy()

		currentProgress += 1 + getAmountOfOverclocks()

		while (currentProgress >= maxProgress) {
			currentProgress -= maxOf(1, maxProgress)
			craftItem()
		}
	}

	protected fun outputSlotHasRoom(potentialOutput: ItemStack): Boolean {
		val currentOutput = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)
		if (currentOutput.isEmpty) return true

		if (!ItemStack.isSameItemSameComponents(potentialOutput, currentOutput)) return false

		val combinedCount = currentOutput.count + potentialOutput.count
		return combinedCount <= currentOutput.maxStackSize
	}

	protected open val inputHandler by lazy {
		insertOnlyHandler(INPUT_SLOT_INDEX)
	}
	protected open val outputHandler by lazy {
		extractOnlyHandler(OUTPUT_SLOT_INDEX)
	}
	protected open val overclockHandler by lazy {
		insertAndExtractHandler(OVERCLOCK_SLOT_INDEX)
	}

	override fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource>? {
		val blockFacing = this.blockState.getValue(MachineBlock.H_FACING)

		return when (direction) {
			blockFacing.opposite -> overclockHandler
			Direction.DOWN -> outputHandler
			else -> inputHandler
		}
	}

	override fun saveAdditional(output: ValueOutput) {
		super.saveAdditional(output)

		output.putInt(CURRENT_PROGRESS_NBT, currentProgress)
		output.putInt(MAX_PROGRESS_NBT, maxProgress)
	}

	override fun loadAdditional(input: ValueInput) {
		super.loadAdditional(input)

		currentProgress = input.getIntOr(CURRENT_PROGRESS_NBT, currentProgress)
		maxProgress = input.getIntOr(MAX_PROGRESS_NBT, maxProgress)
	}

	companion object {
		const val CURRENT_PROGRESS_NBT = "CurrentProgress"
		const val MAX_PROGRESS_NBT = "MaxProgress"

		const val DEFAULT_MAX_PROGRESS = 20 * 4

		const val DEFAULT_INVENTORY_SIZE = 3
		const val INPUT_SLOT_INDEX = 0
		const val OUTPUT_SLOT_INDEX = 1
		const val OVERCLOCK_SLOT_INDEX = 2
	}

}
