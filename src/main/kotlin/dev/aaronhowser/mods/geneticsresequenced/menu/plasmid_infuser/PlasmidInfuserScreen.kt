package dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_infuser

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.geneticsresequenced.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.menu.dna_decryptor.DnaDecryptorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.dna_extractor.DnaExtractorMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class PlasmidInfuserScreen(
	menu: PlasmidInfuserMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<PlasmidInfuserMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = ScreenTextures.Backgrounds.PLASMID_INFUSER

}