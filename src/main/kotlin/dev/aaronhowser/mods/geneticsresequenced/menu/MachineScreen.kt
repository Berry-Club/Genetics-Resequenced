package dev.aaronhowser.mods.geneticsresequenced.menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.components.EnergyBar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

abstract class MachineScreen<T : AbstractContainerMenu>(
	menu: T,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<T>(menu, playerInventory, title) {

	protected lateinit var energyBar: EnergyBar
	protected

	override fun baseInit() {
		super.baseInit()
	}

}