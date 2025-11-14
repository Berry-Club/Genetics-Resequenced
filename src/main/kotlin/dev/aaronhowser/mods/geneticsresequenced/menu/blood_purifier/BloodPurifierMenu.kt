package dev.aaronhowser.mods.geneticsresequenced.menu.blood_purifier

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot

class BloodPurifierMenu(
	containerId: Int,
	playerInventory: Inventory,
	private val bloodPurifierContainer: Container,
	energyContainerData: ContainerData,
	progressContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.BLOOD_PURIFIER.get(), containerId, playerInventory, energyContainerData, progressContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE),
		SimpleContainerData(MachineBlockEntity.ENERGY_CONTAINER_DATA_SIZE),
		SimpleContainerData(CraftingMachineBlockEntity.PROGRESS_CONTAINER_DATA_SIZE)
	)

	init {
		checkContainerSize(bloodPurifierContainer, CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE)

		addPlayerInventorySlots(inventoryY)

		addSlots()
	}

	override fun addSlots() {
		val inputSlot = Slot(bloodPurifierContainer, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 42)
		val outputSlot = Slot(bloodPurifierContainer, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 42)
		val overclockerSlot = Slot(bloodPurifierContainer, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 26, 54)

		addSlot(inputSlot)
		addSlot(outputSlot)
		addSlot(overclockerSlot)
	}

	override fun stillValid(player: Player): Boolean {
		return bloodPurifierContainer.stillValid(player)
	}
}