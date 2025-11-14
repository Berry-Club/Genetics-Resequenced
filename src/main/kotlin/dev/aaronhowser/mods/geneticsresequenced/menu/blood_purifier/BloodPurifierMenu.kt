package dev.aaronhowser.mods.geneticsresequenced.menu.blood_purifier

import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData

class BloodPurifierMenu(
	containerId: Int,
	playerInventory: Inventory,
	private val bloodPurifierContainer: Container,
	energyContainerData: ContainerData,
	progressContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.BLOOD_PURIFIER.get(), containerId, playerInventory, energyContainerData, progressContainerData) {

	override fun stillValid(player: Player): Boolean {
		return bloodPurifierContainer.stillValid(player)
	}
}