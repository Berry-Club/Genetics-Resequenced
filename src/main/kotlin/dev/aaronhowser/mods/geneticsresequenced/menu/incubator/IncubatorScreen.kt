package dev.aaronhowser.mods.geneticsresequenced.menu.incubator

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.geneticsresequenced.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class IncubatorScreen(
	menu: IncubatorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<IncubatorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = ScreenTextures.Backgrounds.INCUBATOR

}