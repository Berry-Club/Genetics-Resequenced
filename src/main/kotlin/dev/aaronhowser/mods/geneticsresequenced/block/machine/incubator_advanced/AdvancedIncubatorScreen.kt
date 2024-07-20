package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class AdvancedIncubatorScreen(
    pMenu: AdvancedIncubatorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<AdvancedIncubatorMenu>(pMenu, pPlayerInventory, pTitle) {

    companion object {
        private const val FAST_BUBBLE_SPEED = 12
        private const val SLOW_BUBBLE_SPEED = 12 * 3
    }

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        pGuiGraphics.blit(
            ScreenTextures.Backgrounds.INCUBATOR_ADVANCED,
            leftPos, topPos,
            0, 0,
            ScreenTextures.Backgrounds.TEXTURE_SIZE,
            ScreenTextures.Backgrounds.TEXTURE_SIZE
        )

        renderHeat(pGuiGraphics, leftPos, topPos)
        renderProgressArrow(pGuiGraphics, leftPos, topPos)
        renderBubble(pGuiGraphics, leftPos, topPos)
        renderEnergyInfo(pGuiGraphics, leftPos, topPos)
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
            x + ScreenTextures.Elements.Energy.Location.Incubator.X,
            y + ScreenTextures.Elements.Energy.Location.Incubator.Y,
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
                ScreenTextures.Elements.Energy.Location.Incubator.X,
                ScreenTextures.Elements.Energy.Location.Incubator.Y,
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

    override fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int): Boolean {
        if (isMouseOverTemperature(pMouseX.toInt(), pMouseY.toInt(), leftPos, topPos)) {
            this.minecraft?.gameMode?.handleInventoryButtonClick(this.menu.containerId, 1)
            bubblePosProgress = 0
            bubblePos = 0
            return true
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton)
    }

    private fun isMouseOverTemperature(mouseX: Int, mouseY: Int, x: Int, y: Int): Boolean {
        return isMouseOver(
            mouseX, mouseY,
            x, y,
            ScreenTextures.Elements.Heat.Position.X,
            ScreenTextures.Elements.Heat.Position.Y,
            ScreenTextures.Elements.Heat.Dimensions.WIDTH,
            ScreenTextures.Elements.Heat.Dimensions.HEIGHT
        )
    }

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

    //FIXME: Make it fill up instead of emptying
    private fun renderProgressArrow(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!menu.isCrafting) return

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.ArrowDown.TEXTURE,
            ScreenTextures.Elements.ArrowDown.TEXTURE_SIZE,
            ScreenTextures.Elements.ArrowDown.TEXTURE_SIZE,
            0,
            0,
            x + ScreenTextures.Elements.ArrowDown.Position.X,
            y + ScreenTextures.Elements.ArrowDown.Position.Y,
            ScreenTextures.Elements.ArrowDown.Dimensions.WIDTH,
            menu.getScaledProgress()
        )
    }

    private var bubblePosProgress = 0
    private var bubblePos = 0
        set(value) {
            field = value

            val amountOverMax = bubblePos - ScreenTextures.Elements.Bubbles.Dimensions.HEIGHT
            if (amountOverMax > 0) {
                field = amountOverMax
            }
        }

    //FIXME: Make the bubbles go up instead of down
    private fun renderBubble(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!menu.isCrafting) return

        val speed = if (menu.isHighTemperature) FAST_BUBBLE_SPEED else SLOW_BUBBLE_SPEED

        bubblePosProgress++
        if (bubblePosProgress % speed == 0) {
            bubblePos++
            bubblePosProgress = 0
        }

        val amountBubbleToRender = ScreenTextures.Elements.Bubbles.Dimensions.HEIGHT - bubblePos

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Bubbles.TEXTURE,
            ScreenTextures.Elements.Bubbles.TEXTURE_SIZE,
            ScreenTextures.Elements.Bubbles.TEXTURE_SIZE,
            0,
            0,
            x + ScreenTextures.Elements.Bubbles.Position.X,
            y + ScreenTextures.Elements.Bubbles.Position.Y,
            ScreenTextures.Elements.Bubbles.Dimensions.WIDTH,
            amountBubbleToRender
        )
    }

    private fun renderHeat(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        val hasEnergy = menu.blockEntity.energyStorage.energyStored != 0
        if (!hasEnergy) return

        val texture =
            if (menu.isHighTemperature) ScreenTextures.Elements.Heat.Texture.HIGH else ScreenTextures.Elements.Heat.Texture.LOW

        pGuiGraphics.blitSprite(
            texture,
            ScreenTextures.Elements.Heat.TEXTURE_SIZE,
            ScreenTextures.Elements.Heat.TEXTURE_SIZE,
            0, 0,
            x + ScreenTextures.Elements.Heat.Position.X,
            y + ScreenTextures.Elements.Heat.Position.Y,
            ScreenTextures.Elements.Heat.Dimensions.WIDTH,
            ScreenTextures.Elements.Heat.Dimensions.HEIGHT
        )
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        super.renderTooltip(pGuiGraphics, pMouseX, pMouseY)
    }

}