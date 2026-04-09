package dev.aaronhowser.mods.genetics_resequenced.menu.incubator

import dev.aaronhowser.mods.genetics_resequenced.block.base.container_data.CraftingContainerData
import dev.aaronhowser.mods.genetics_resequenced.block.block_entity.IncubatorBlockEntity
import dev.aaronhowser.mods.genetics_resequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.genetics_resequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot

class IncubatorMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	craftingContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.INCUBATOR.get(), containerId, playerInventory, machineContainer, craftingContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(IncubatorBlockEntity.INVENTORY_SIZE),
		SimpleContainerData(CraftingContainerData.CRAFTING_CONTAINER_DATA_SIZE)
	)

	override val amountSlots: Int = IncubatorBlockEntity.INVENTORY_SIZE

	init {
		addSlots()
	}

	override fun addSlots() {
		val topSlot = Slot(machineContainer, IncubatorBlockEntity.TOP_SLOT_INDEX, 83, 21)
		val leftBottleSlot = Slot(machineContainer, IncubatorBlockEntity.LEFT_BOTTLE_SLOT_INDEX, 60, 55)
		val middleBottleSlot = Slot(machineContainer, IncubatorBlockEntity.MIDDLE_BOTTLE_SLOT_INDEX, 83, 62)
		val rightBottleSlot = Slot(machineContainer, IncubatorBlockEntity.RIGHT_BOTTLE_SLOT_INDEX, 106, 55)
		val overclockerSlot = Slot(machineContainer, IncubatorBlockEntity.OVERCLOCKER_SLOT_INDEX, 141, 38)

		this.addSlot(topSlot)
		this.addSlot(leftBottleSlot)
		this.addSlot(middleBottleSlot)
		this.addSlot(rightBottleSlot)
		this.addSlot(overclockerSlot)
	}

}