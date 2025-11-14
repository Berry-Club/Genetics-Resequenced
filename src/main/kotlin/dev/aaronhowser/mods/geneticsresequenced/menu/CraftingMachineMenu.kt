package dev.aaronhowser.mods.geneticsresequenced.menu

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType

abstract class CraftingMachineMenu(
	menuType: MenuType<*>,
	id: Int,
	playerInventory: Inventory,
	energyContainerData: ContainerData,
	protected val progressContainerData: ContainerData
) : MachineMenu(menuType, id, playerInventory, energyContainerData) {

	override val amountSlots: Int = CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE

	var currentProgress: Int
		get() = progressContainerData.get(CraftingMachineBlockEntity.CURRENT_PROGRESS_INDEX)
		set(value) = progressContainerData.set(CraftingMachineBlockEntity.CURRENT_PROGRESS_INDEX, value)

	var maxProgress: Int
		get() = progressContainerData.get(CraftingMachineBlockEntity.MAX_PROGRESS_INDEX)
		set(value) = progressContainerData.set(CraftingMachineBlockEntity.MAX_PROGRESS_INDEX, value)

	fun isCrafting(): Boolean = currentProgress > 0

	override fun getPercentDone(): Float {
		if (maxProgress <= 0) return 0f
		return currentProgress.toFloat() / maxProgress.toFloat()
	}

}