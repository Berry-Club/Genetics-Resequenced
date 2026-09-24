package dev.aaronhowser.mods.genetics_resequenced.menu.dna_decryptor

import dev.aaronhowser.mods.genetics_resequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.genetics_resequenced.block.base.container_data.CraftingContainerData
import dev.aaronhowser.mods.genetics_resequenced.item.EntityDnaItem
import dev.aaronhowser.mods.genetics_resequenced.item.GeneItemData
import dev.aaronhowser.mods.genetics_resequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.genetics_resequenced.registry.ModMenuTypes
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
		return EntityDnaItem.hasEntity(inputStack) && !GeneItemData.hasGene(inputStack)
	}

}