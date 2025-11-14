package dev.aaronhowser.mods.geneticsresequenced.block.base

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.InvWrapper
import net.neoforged.neoforge.items.wrapper.RangedWrapper
import java.util.function.IntSupplier

abstract class CraftingMachineBlockEntity(
	blockEntityType: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : MachineBlockEntity(blockEntityType, pos, blockState) {

	abstract val baseEnergyCostPerTick: IntSupplier

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
		if (energyStorage.energyStored <= getEnergyCostPerTick()) return false
		energyStorage.extractEnergy(getEnergyCostPerTick(), false)
		return true
	}

	protected var currentProgress: Int = 0
		set(value) {
			field = value
			setChanged()
		}

	protected var maxProgress: Int = 0
		set(value) {
			field = value
			setChanged()
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
			currentProgress -= maxProgress
			craftItem()
		}
	}

	protected open val inputHandler = RangedWrapper(invWrapper, INPUT_SLOT_INDEX, INPUT_SLOT_INDEX + 1)
	protected open val outputHandler = RangedWrapper(invWrapper, OUTPUT_SLOT_INDEX, OUTPUT_SLOT_INDEX + 1)
	protected open val overclockHandler = RangedWrapper(invWrapper, OVERCLOCK_SLOT_INDEX, OVERCLOCK_SLOT_INDEX + 1)

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

		const val SIMPLE_CONTAINER_SIZE = 2
		const val ITEMSTACK_HANDLER_SIZE = 3

		const val INPUT_SLOT_INDEX = 0
		const val OUTPUT_SLOT_INDEX = 1
		const val OVERCLOCK_SLOT_INDEX = 2
	}

}