package dev.aaronhowser.mods.geneticsresequenced.menu.blood_purifier

import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData

class BloodPurifierMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	energyContainerData: ContainerData,
	progressContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.BLOOD_PURIFIER.get(), containerId, playerInventory, machineContainer, energyContainerData, progressContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE),
		SimpleContainerData(MachineBlockEntity.ENERGY_CONTAINER_DATA_SIZE),
		SimpleContainerData(CraftingMachineBlockEntity.PROGRESS_CONTAINER_DATA_SIZE)
	)

	init {
		checkContainerSize(machineContainer, CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE)
		addSlots()
	}

	override fun addSlots() {
		val inputSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 42) { SyringeItem.isContaminated(it) }
		val outputSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 42)
		val overclockSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 26, 54) { it.`is`(ModItems.OVERCLOCKER) }

		this.addSlot(inputSlot)
		this.addSlot(outputSlot)
		this.addSlot(overclockSlot)
	}

}