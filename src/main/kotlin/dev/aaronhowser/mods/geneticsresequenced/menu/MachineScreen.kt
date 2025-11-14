package dev.aaronhowser.mods.geneticsresequenced.menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

abstract class MachineScreen<T : AbstractContainerMenu>(
	menu: T,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<T>(menu, playerInventory, title) {

	override fun baseInit() {
		super.baseInit()
	}

}