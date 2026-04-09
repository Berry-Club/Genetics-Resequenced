package dev.aaronhowser.mods.genetics_resequenced.menu.dna_extractor

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.genetics_resequenced.menu.MachineScreen
import dev.aaronhowser.mods.genetics_resequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class DnaExtractorScreen(
	menu: DnaExtractorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<DnaExtractorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = ScreenTextures.Backgrounds.DNA_EXTRACTOR

}