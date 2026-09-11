package dev.aaronhowser.mods.genetics_resequenced.menu

import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.menu.components.OutputSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.container_data.EnergyProgressContainerData
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack

abstract class CraftingMachineMenu(
	menuType: MenuType<*>,
	id: Int,
	playerInventory: Inventory,
	protected val machineContainer: Container,
	craftingContainerData: ContainerData
) : MachineMenu(menuType, id, playerInventory, craftingContainerData) {

	override val amountSlots: Int = CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE

	init {
		checkContainerSize(machineContainer, amountSlots)
	}

	protected open fun inputFilter(inputStack: ItemStack): Boolean = true

	override fun addContainerSlots() {
		val inputSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 42, ::inputFilter)
		val outputSlot = OutputSlot(machineContainer, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 42)
		val overclockSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 26, 54) { it.isItem(ModItems.OVERCLOCKER) }

		this.addSlot(inputSlot)
		this.addSlot(outputSlot)
		this.addSlot(overclockSlot)
	}

	fun getCurrentProgress(): Int = machineContainerData.get(EnergyProgressContainerData.CURRENT_PROGRESS_INDEX)
	fun getMaxProgress(): Int = machineContainerData.get(EnergyProgressContainerData.MAX_PROGRESS_INDEX)

	fun isCrafting(): Boolean = getCurrentProgress() > 0

	override fun getPercentDone(): Float {
		val max = getMaxProgress()
		if (max <= 0) return 0f

		return getCurrentProgress().toFloat() / max.toFloat()
	}

	override fun stillValid(player: Player): Boolean {
		return machineContainer.stillValid(player)
	}

}