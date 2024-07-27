package dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_decryptor

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class DnaDecryptorScreen(
    pMenu: DnaDecryptorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<DnaDecryptorMenu>(pMenu, pPlayerInventory, pTitle) {
    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.DNA_DECRYPTOR

    override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting
    override fun progressArrowWidth(): Int = menu.getScaledProgress()
    override fun progressArrowHeight(): Int = ScreenTextures.Elements.ArrowRight.Dimensions.HEIGHT
}