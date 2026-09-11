package dev.aaronhowser.mods.geneticsresequenced.menu.dna_decryptor

import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.hasComponent
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.container_data.CraftingContainerData
import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeDataComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneDataComponent
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

class DnaDecryptorMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	craftingContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.DNA_DECRYPTOR.get(), containerId, playerInventory, machineContainer, craftingContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE),
		SimpleContainerData(CraftingContainerData.CRAFTING_CONTAINER_DATA_SIZE)
	)

	init {
		addSlots(inventoryY)
	}

	override fun inputFilter(inputStack: ItemStack): Boolean {
		return inputStack.hasComponent(EntityTypeDataComponent.Type) && !inputStack.hasComponent(GeneDataComponent.Type)
	}

}