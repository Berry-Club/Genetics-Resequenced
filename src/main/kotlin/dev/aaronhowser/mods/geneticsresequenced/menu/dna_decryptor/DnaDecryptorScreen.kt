package dev.aaronhowser.mods.geneticsresequenced.menu.dna_decryptor

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.geneticsresequenced.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class DnaDecryptorScreen(
	menu: DnaDecryptorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<DnaDecryptorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = ScreenTextures.Backgrounds.DNA_DECRYPTOR

}