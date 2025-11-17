package dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_infuser

import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.menu.components.OutputSlot
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

class PlasmidInfuserMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	energyContainerData: ContainerData,
	progressContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.PLASMID_INFUSER.get(), containerId, playerInventory, machineContainer, energyContainerData, progressContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE),
		SimpleContainerData(MachineBlockEntity.ENERGY_CONTAINER_DATA_SIZE),
		SimpleContainerData(CraftingMachineBlockEntity.PROGRESS_CONTAINER_DATA_SIZE)
	)

	init {
		addSlots()
	}

	override fun inputFilter(inputStack: ItemStack): Boolean {
		return inputStack.`is`(ModItems.DNA_HELIX) && inputStack.has(ModDataComponents.GENE)
	}

	override fun addSlots() {
		val helixSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 42, ::inputFilter)
		val plasmidSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 42) { it.`is`(ModItems.PLASMID) }
		val overclockSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 26, 54) { it.`is`(ModItems.OVERCLOCKER) }

		this.addSlot(helixSlot)
		this.addSlot(plasmidSlot)
		this.addSlot(overclockSlot)
	}

}