package dev.aaronhowser.mods.geneticsresequenced.block.base

import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.RangedWrapper
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
		return energyStorage.energyStored >= getEnergyCostPerTick()
	}

	protected fun drainEnergy(): Boolean {
		val cost = getEnergyCostPerTick()
		if (energyStorage.energyStored < cost) return false
		energyStorage.extractEnergy(cost, false)
		return true
	}

	protected var currentProgress: Int = 0
		set(value) {
			if (field != value) {
				field = value
				setChanged()
			}
		}

	protected var maxProgress: Int = 20 * 4
		set(value) {
			if (field != value) {
				field = value
				setChanged()
			}
		}

	protected val progressContainerData = object : ContainerData {
		override fun get(index: Int): Int {
			return when (index) {
				CURRENT_PROGRESS_INDEX -> currentProgress
				MAX_PROGRESS_INDEX -> maxProgress
				else -> -1
			}
		}

		override fun set(index: Int, value: Int) {
			when (index) {
				CURRENT_PROGRESS_INDEX -> currentProgress = value
				MAX_PROGRESS_INDEX -> maxProgress = value
			}
		}

		override fun getCount(): Int = PROGRESS_CONTAINER_DATA_SIZE
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

	protected open val inputHandler = RangedWrapper(itemHandler, INPUT_SLOT_INDEX, INPUT_SLOT_INDEX + 1)
	protected open val outputHandler = RangedWrapper(itemHandler, OUTPUT_SLOT_INDEX, OUTPUT_SLOT_INDEX + 1)
	protected open val overclockHandler = RangedWrapper(itemHandler, OVERCLOCK_SLOT_INDEX, OVERCLOCK_SLOT_INDEX + 1)

	override fun getItemHandler(direction: Direction?): IItemHandler? {
		val blockFacing = this.blockState.getValue(MachineBlock.H_FACING)

		return when (direction) {
			blockFacing.opposite -> overclockHandler
			Direction.DOWN -> outputHandler
			else -> inputHandler
		}
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(CURRENT_PROGRESS_NBT, currentProgress)
		tag.putInt(MAX_PROGRESS_NBT, maxProgress)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		currentProgress = tag.getInt(CURRENT_PROGRESS_NBT)
		maxProgress = tag.getInt(MAX_PROGRESS_NBT)
	}

	companion object {
		const val CURRENT_PROGRESS_NBT = "CurrentProgress"
		const val MAX_PROGRESS_NBT = "MaxProgress"

		const val PROGRESS_CONTAINER_DATA_SIZE = 2
		const val CURRENT_PROGRESS_INDEX = 0
		const val MAX_PROGRESS_INDEX = 1

		const val DEFAULT_INVENTORY_SIZE = 3
		const val INPUT_SLOT_INDEX = 0
		const val OUTPUT_SLOT_INDEX = 1
		const val OVERCLOCK_SLOT_INDEX = 2
	}

}