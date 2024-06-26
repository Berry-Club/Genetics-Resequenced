package dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator

import com.mojang.blaze3d.systems.RenderSystem
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class CoalGeneratorScreen(
    pMenu: CoalGeneratorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<CoalGeneratorMenu>(pMenu, pPlayerInventory, pTitle) {

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
            ScreenTextures.Backgrounds.TEXTURE_SIZE,
            ScreenTextures.Backgrounds.TEXTURE_SIZE
        )

        renderProgressArrow(pGuiGraphics, x, y)
        renderBurnProgress(pGuiGraphics, x, y)
        renderEnergyInfo(pGuiGraphics, x, y)
    }

    private fun renderEnergyInfo(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        val energyStorage = menu.blockEntity.energyStorage
        val percent = energyStorage.energyStored.toFloat() / energyStorage.maxEnergyStored.toFloat()

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Energy.TEXTURE,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            0,
            0,
            x + ScreenTextures.Elements.Energy.Location.CoalGen.X,
            y + ScreenTextures.Elements.Energy.Location.CoalGen.Y,
            0,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            (ScreenTextures.Elements.Energy.TEXTURE_SIZE * percent).toInt()
        )
    }

    override fun renderLabels(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {

        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2
        renderEnergyAreaTooltip(pGuiGraphics, x, y, pMouseX, pMouseY)

        super.renderLabels(pGuiGraphics, pMouseX, pMouseY)
    }

    private fun renderEnergyAreaTooltip(pGuiGraphics: GuiGraphics, x: Int, y: Int, pMouseX: Int, pMouseY: Int) {
        if (isMouseOver(
                pMouseX, pMouseY,
                x, y,
                ScreenTextures.Elements.Energy.Location.CoalGen.X,
                ScreenTextures.Elements.Energy.Location.CoalGen.Y,
                ScreenTextures.Elements.Energy.Dimensions.WIDTH,
                ScreenTextures.Elements.Energy.Dimensions.HEIGHT
            )
        ) {
            val energyStorage = menu.blockEntity.energyStorage

            pGuiGraphics.renderComponentTooltip(
                Minecraft.getInstance().font,
                Component
                    .literal(energyStorage.energyStored.toString() + "/" + energyStorage.maxEnergyStored + " FE")
                    .toFlatList(),
                pMouseX - x,
                pMouseY - y
            )
        }

    }

    @Suppress("SameParameterValue")
    private fun isMouseOver(
        mouseX: Int, mouseY: Int,
        x: Int, y: Int,
        topLeftX: Int,
        topLeftY: Int,
        width: Int,
        height: Int
    ): Boolean {
        return MouseUtil.isMouseOver(
            mouseX, mouseY,
            x + topLeftX, y + topLeftY,
            width, height
        )
    }

    private fun renderProgressArrow(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!menu.isBurning) return

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.ArrowRight.TEXTURE,
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE,
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE,
            0,
            0,
            x + ScreenTextures.Elements.ArrowRight.Position.CoalGen.X,
            y + ScreenTextures.Elements.ArrowRight.Position.CoalGen.Y,
            menu.getScaledProgressArrow(),
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE
        )
    }

    //TODO: Goes up instead of down
    private fun renderBurnProgress(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (menu.isBurning) {
            val fuelRemaining = menu.getScaledFuelRemaining()
            pGuiGraphics.blitSprite(
                ScreenTextures.Elements.Burn.TEXTURE,
                ScreenTextures.Elements.Burn.TEXTURE_SIZE,
                ScreenTextures.Elements.Burn.TEXTURE_SIZE,
                0,
                0,
                x + ScreenTextures.Elements.Burn.Position.X,
                y + ScreenTextures.Elements.Burn.Position.Y,
                ScreenTextures.Elements.Burn.TEXTURE_SIZE,
                fuelRemaining
            )
        }
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        renderTooltip(pGuiGraphics, pMouseX, pMouseY)
    }

}