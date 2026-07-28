package dev.aaronhowser.mods.geneticsresequenced.block_entity.base.container_data

import net.neoforged.neoforge.energy.EnergyStorage
import java.util.function.IntSupplier

open class EnergyProgressContainerData(
	energyStorage: EnergyStorage,
	private val currentProgressGetter: IntSupplier,
	private val maxProgressGetter: IntSupplier,
) : EnergyContainerData(energyStorage) {

	override fun get(index: Int): Int {
		return when (index) {
			CURRENT_PROGRESS_INDEX -> currentProgressGetter.asInt
			MAX_PROGRESS_INDEX -> maxProgressGetter.asInt
			else -> super.get(index)
		}
	}

	override fun set(index: Int, value: Int) {
		// Cannot set from container data
	}

	override fun getCount(): Int = ENERGY_PROGRESS_CONTAINER_DATA_SIZE

	companion object {
		const val ENERGY_PROGRESS_CONTAINER_DATA_SIZE = 4

		const val CURRENT_PROGRESS_INDEX = 2
		const val MAX_PROGRESS_INDEX = 3
	}

}