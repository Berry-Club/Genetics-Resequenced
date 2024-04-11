package dev.aaronhowser.mods.geneticsresequenced.screens

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory


class CellAnalyzerScreen(
    pMenu: CellAnalyzerMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<CellAnalyzerMenu>(pMenu, pPlayerInventory, pTitle) {

    companion object {
        val BACKGROUND_TEXTURE = ResourceLocation(GeneticsResequenced.ID, "textures/gui/cell_analyzer.png")
    }

    override fun init() {
        super.init()
    }

    override fun renderBg(pPoseStack: PoseStack, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE)
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight)

        renderProgressArrow(pPoseStack, x, y)
    }

    private fun renderProgressArrow(pPoseStack: PoseStack, x: Int, y: Int) {
        if (menu.isCrafting) {
            blit(
                pPoseStack,
                x + 83,                 // The x position of where the arrow will be
                y + 37,                 // The y position of where the arrow will be
                177,                // The x offset of where the arrow is in the texture
                61,                 // The y offset of where the arrow is in the texture
                24,                 // The width of the arrow
                menu.getScaledProgress()    // How much of the arrow to render
            )
        }
    }

    override fun render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderBackground(pPoseStack)
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
        renderTooltip(pPoseStack, pMouseX, pMouseY)
    }
}