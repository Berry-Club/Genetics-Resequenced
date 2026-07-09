package dev.aaronhowser.mods.genetics_resequenced.menu.dna_extractor

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.container_data.EnergyProgressContainerData
import dev.aaronhowser.mods.genetics_resequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

class DnaExtractorMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	craftingContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.DNA_EXTRACTOR.get(), containerId, playerInventory, machineContainer, craftingContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE),
		SimpleContainerData(EnergyProgressContainerData.ENERGY_PROGRESS_CONTAINER_DATA_SIZE)
	)

	init {
		addSlots()
	}

	override fun inputFilter(inputStack: ItemStack): Boolean = inputStack.isItem(ModItems.CELL) || inputStack.isItem(ModItems.GMO_CELL)

}