package dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator

import com.mojang.blaze3d.systems.RenderSystem
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class CoalGeneratorScreen(
    pMenu: CoalGeneratorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<CoalGeneratorMenu>(pMenu, pPlayerInventory, pTitle) {

//    private lateinit var energyInfoArea: EnergyInfoArea

    override fun init() {
        super.init()
        assignInfoArea()
    }

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, ScreenTextures.Backgrounds.COAL_GENERATOR)
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2

        pGuiGraphics.blit(
            ScreenTextures.Backgrounds.COAL_GENERATOR,
            x,
            y,
            0,
            0,
            176,
            166
        )

        renderProgressArrow(pGuiGraphics, x, y)
        renderBurnProgress(pGuiGraphics, x, y)
//        energyInfoArea.draw(pGuiGraphics)
    }

    private fun assignInfoArea() {
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2

//        energyInfoArea = EnergyInfoArea(
//            x + ENERGY_X,
//            y + ENERGY_Y,
//            menu.blockEntity.energyStorage,
//            ENERGY_WIDTH,
//            ENERGY_HEIGHT
//        )

    }

    override fun renderLabels(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {

        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2
//        renderEnergyAreaTooltip(pGuiGraphics, x, y, pMouseX, pMouseY)

        super.renderLabels(pGuiGraphics, pMouseX, pMouseY)
    }

//    private fun renderEnergyAreaTooltip(pGuiGraphics: GuiGraphics, x: Int, y: Int, pMouseX: Int, pMouseY: Int) {
//
//        if (isMouseOver(pMouseX, pMouseY, x, y, ENERGY_X, ENERGY_Y, ENERGY_WIDTH, ENERGY_HEIGHT)) {
//            renderTooltip(pGuiGraphics, energyInfoArea.tooltip, pMouseX - x, pMouseY - y)
//        }
//
//    }

    private fun isMouseOver(
        mouseX: Int,
        mouseY: Int,
        x: Int,
        y: Int,
        offsetX: Int,
        offsetY: Int,
        width: Int,
        height: Int
    ): Boolean {
        return MouseUtil.isMouseOver(
            mouseX.toDouble(),
            mouseY.toDouble(),
            x + offsetX,
            y + offsetY,
            width,
            height
        )
    }

    private fun renderProgressArrow(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (menu.isBurning) {
            pGuiGraphics.blitSprite(
                ScreenTextures.Elements.ArrowRight.TEXTURE,
                x + ScreenTextures.Elements.ArrowRight.POSITION.x,
                y + ScreenTextures.Elements.ArrowRight.POSITION.y,
                ScreenTextures.Elements.ArrowRight.DIMENSIONS.x,
                ScreenTextures.Elements.ArrowRight.DIMENSIONS.y,
            )
        }
    }

    private fun renderBurnProgress(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (menu.isBurning) {
            val fuelRemaining = menu.getScaledFuelRemaining()
            pGuiGraphics.blitSprite(
                ScreenTextures.Elements.Burn.TEXTURE,
                x + ScreenTextures.Elements.Burn.POSITION.x,
                y + ScreenTextures.Elements.Burn.POSITION.y,
                ScreenTextures.Elements.Burn.DIMENSIONS.x,
                ScreenTextures.Elements.Burn.DIMENSIONS.y,
            )
        }
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        renderTooltip(pGuiGraphics, pMouseX, pMouseY)
    }

}