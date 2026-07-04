package dev.aaronhowser.mods.geneticsresequenced.menu.dna_extractor

import dev.aaronhowser.mods.geneticsresequenced.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class DnaExtractorScreen(
	menu: DnaExtractorMenu,
	playerInventory: Inventory,
	title: Component
) : MachineScreen<DnaExtractorMenu>(menu, playerInventory, title, ScreenTextures.Backgrounds.DNA_EXTRACTOR)
