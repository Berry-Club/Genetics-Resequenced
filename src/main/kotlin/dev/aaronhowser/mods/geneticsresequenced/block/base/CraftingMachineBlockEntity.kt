package dev.aaronhowser.mods.geneticsresequenced.block.base

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

abstract class CraftingMachineBlockEntity(
	blockEntityType: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : MachineBlockEntity(blockEntityType, pos, blockState) {

	abstract val baseEnergyCostPerTick: IntSupplier

	open fun getEnergyCostPerTick() {

	}

	open fun getAmountOfOverclocks(): Int {
		return container.getItem(OVERCLOCK_SLOT_INDEX).count
	}

	companion object {
		const val SIMPLE_CONTAINER_SIZE = 2
		const val ITEMSTACK_HANDLER_SIZE = 3

		const val INPUT_SLOT_INDEX = 0
		const val OUTPUT_SLOT_INDEX = 1
		const val OVERCLOCK_SLOT_INDEX = 2
	}

}