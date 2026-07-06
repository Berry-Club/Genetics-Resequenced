package dev.aaronhowser.mods.genetics_resequenced.block_entity.base.container_data

import net.minecraft.world.inventory.ContainerData
import net.neoforged.neoforge.transfer.energy.EnergyHandler

open class EnergyContainerData(
	val energyStorage: EnergyHandler
) : ContainerData {

	override fun get(index: Int): Int {
		return when (index) {
			CURRENT_ENERGY_INDEX -> energyStorage.amountAsInt
			MAX_ENERGY_INDEX -> energyStorage.capacityAsInt
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
