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

	protected open val inventoryX = 8
	protected open val inventoryY = 90
	protected abstract val amountSlots: Int

	init {
		addPlayerInventorySlots()
		addDataSlots(machineContainerData)
	}

	fun getCurrentEnergy(): Int = machineContainerData.get(EnergyContainerData.CURRENT_ENERGY_INDEX)
	fun getMaxEnergy(): Int = machineContainerData.get(EnergyContainerData.MAX_ENERGY_INDEX)

	fun addPlayerInventorySlots() {
		addPlayerInventorySlots(inventoryY)
	}

	abstract fun getPercentDone(): Float

	companion object {
		private const val HOTBAR_SLOT_COUNT = 9
		private const val PLAYER_INVENTORY_ROW_COUNT = 3
		private const val PLAYER_INVENTORY_COLUMN_COUNT = 9
		private const val PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT
		private const val VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT
		private const val VANILLA_FIRST_SLOT_INDEX = 0
		private const val TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT
	}

}