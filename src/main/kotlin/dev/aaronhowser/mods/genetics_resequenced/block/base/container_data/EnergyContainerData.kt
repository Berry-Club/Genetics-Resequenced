package dev.aaronhowser.mods.genetics_resequenced.block.base.container_data

import net.minecraft.world.inventory.ContainerData
import net.neoforged.neoforge.energy.EnergyStorage

open class EnergyContainerData(
	val energyStorage: EnergyStorage
) : ContainerData {

	override fun get(index: Int): Int {
		return when (index) {
			CURRENT_ENERGY_INDEX -> energyStorage.energyStored
			MAX_ENERGY_INDEX -> energyStorage.maxEnergyStored
			else -> -1
		}
	}

	override fun set(index: Int, value: Int) {
		// Cannot set from container data
	}

	override fun getCount(): Int = ENERGY_CONTAINER_DATA_SIZE

	companion object {
		const val ENERGY_CONTAINER_DATA_SIZE = 2
		const val CURRENT_ENERGY_INDEX = 0
		const val MAX_ENERGY_INDEX = 1
	}

}