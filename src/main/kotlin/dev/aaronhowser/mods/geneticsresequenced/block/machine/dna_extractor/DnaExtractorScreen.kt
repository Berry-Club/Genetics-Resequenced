package dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_extractor

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class DnaExtractorScreen(
    pMenu: DnaExtractorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<DnaExtractorMenu>(pMenu, pPlayerInventory, pTitle) {
    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.DNA_EXTRACTOR

    override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting
}