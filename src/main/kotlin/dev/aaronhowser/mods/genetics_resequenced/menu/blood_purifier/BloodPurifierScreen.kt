package dev.aaronhowser.mods.genetics_resequenced.menu.blood_purifier

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.genetics_resequenced.menu.MachineScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class BloodPurifierScreen(
	menu: BloodPurifierMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<BloodPurifierMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = ScreenTextures.Backgrounds.BASIC

}