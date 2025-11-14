package dev.aaronhowser.mods.geneticsresequenced.menu.dna_extractor

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.dna_decryptor.DnaDecryptorMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot

class DnaExtractorMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	energyContainerData: ContainerData,
	progressContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.DNA_EXTRACTOR.get(), containerId, playerInventory, machineContainer, energyContainerData, progressContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE),
		SimpleContainerData(MachineBlockEntity.ENERGY_CONTAINER_DATA_SIZE),
		SimpleContainerData(CraftingMachineBlockEntity.PROGRESS_CONTAINER_DATA_SIZE)
	)

	override fun addSlots() {
		val inputSlot = Slot(machineContainer, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 42)
		val outputSlot = Slot(machineContainer, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 42)
		val overclockSlot = Slot(machineContainer, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 26, 54)

		this.addSlot(inputSlot)
		this.addSlot(outputSlot)
		this.addSlot(overclockSlot)
	}

}