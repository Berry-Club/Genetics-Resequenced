package dev.aaronhowser.mods.geneticsresequenced.menu.advanced_incubator

import dev.aaronhowser.mods.aaron.menu.MenuWithButtons
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.container_data.CraftingContainerData
import dev.aaronhowser.mods.geneticsresequenced.block.block_entity.AdvancedIncubatorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot

class AdvancedIncubatorMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	craftingContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.ADVANCED_INCUBATOR.get(), containerId, playerInventory, machineContainer, craftingContainerData), MenuWithButtons {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(AdvancedIncubatorBlockEntity.INVENTORY_SIZE),
		SimpleContainerData(CraftingContainerData.CRAFTING_CONTAINER_DATA_SIZE)
	)

	init {
		addSlots()
	}

	override fun addSlots() {
		val topSlot = Slot(machineContainer, AdvancedIncubatorBlockEntity.TOP_SLOT_INDEX, 83, 21)
		val leftBottleSlot = Slot(machineContainer, AdvancedIncubatorBlockEntity.LEFT_BOTTLE_SLOT_INDEX, 60, 55)
		val middleBottleSlot = Slot(machineContainer, AdvancedIncubatorBlockEntity.MIDDLE_BOTTLE_SLOT_INDEX, 83, 62)
		val rightBottleSlot = Slot(machineContainer, AdvancedIncubatorBlockEntity.RIGHT_BOTTLE_SLOT_INDEX, 106, 55)
		val overclockerSlot = Slot(machineContainer, AdvancedIncubatorBlockEntity.OVERCLOCKER_SLOT_INDEX, 141, 38)
		val chorusSlot = Slot(machineContainer, AdvancedIncubatorBlockEntity.CHORUS_SLOT_INDEX, 141, 60)

		this.addSlot(topSlot)
		this.addSlot(leftBottleSlot)
		this.addSlot(middleBottleSlot)
		this.addSlot(rightBottleSlot)
		this.addSlot(overclockerSlot)
		this.addSlot(chorusSlot)
	}

	override fun handleButtonPressed(buttonId: Int) {
		when (buttonId) {
			CYCLE_TEMPERATURE_BUTTON_ID -> {

			}
		}
	}

	companion object {
		const val CYCLE_TEMPERATURE_BUTTON_ID = 0
	}

}