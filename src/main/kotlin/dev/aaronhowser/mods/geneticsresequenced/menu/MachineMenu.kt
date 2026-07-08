package dev.aaronhowser.mods.geneticsresequenced.menu

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.geneticsresequenced.block_entity.base.container_data.EnergyContainerData
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.level.Level

abstract class MachineMenu(
	menuType: MenuType<*>,
	id: Int,
	playerInventory: Inventory,
	protected val machineContainerData: ContainerData
) : MenuWithInventory(menuType, id, playerInventory) {

	protected val level: Level = playerInventory.player.level()

	init {
		addDataSlots(machineContainerData)
	}

	protected fun addMachineSlots() {
		addSlots(INVENTORY_Y)
	}

	fun getCurrentEnergy(): Int = machineContainerData.get(EnergyContainerData.CURRENT_ENERGY_INDEX)
	fun getMaxEnergy(): Int = machineContainerData.get(EnergyContainerData.MAX_ENERGY_INDEX)

	abstract fun getPercentDone(): Float

	companion object {
		const val INVENTORY_Y = 90
	}

}