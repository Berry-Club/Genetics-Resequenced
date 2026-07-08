package dev.aaronhowser.mods.geneticsresequenced.menu.blood_purifier

import dev.aaronhowser.mods.geneticsresequenced.block_entity.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block_entity.base.container_data.CraftingContainerData
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

class BloodPurifierMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	craftingContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.BLOOD_PURIFIER.get(), containerId, playerInventory, machineContainer, craftingContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE),
		SimpleContainerData(CraftingContainerData.CRAFTING_CONTAINER_DATA_SIZE)
	)

	override fun inputFilter(inputStack: ItemStack): Boolean = SyringeItem.isContaminated(inputStack)

}