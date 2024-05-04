package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_injector

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class PlasmidInjectorScreen(
    private val pMenu: PlasmidInjectorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen(pMenu, pPlayerInventory, pTitle) {

    override val backgroundTexture = OtherUtil.modResource("textures/gui/plasmid_injector.png")

    override fun renderBg(pPoseStack: PoseStack, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY)

        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2
        renderProgressArrow(pPoseStack, x, y)
    }

    private fun renderProgressArrow(pPoseStack: PoseStack, x: Int, y: Int) {
        if (pMenu.isCrafting) {
            blit(
                pPoseStack,
                x + ARROW_X,            // The x position of where the arrow will be
                y + ARROW_Y,            // The y position of where the arrow will be
                ARROW_TEXTURE_X,            // The x offset of where the arrow is in the texture
                ARROW_TEXTURE_Y,               // The y offset of where the arrow is in the texture
                pMenu.getScaledProgress(),   // The width of the arrow
                71                  // The height of the arrow
            )
        }
    }

}