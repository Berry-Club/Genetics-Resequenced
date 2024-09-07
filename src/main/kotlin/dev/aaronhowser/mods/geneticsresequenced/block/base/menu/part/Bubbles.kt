package dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator.IncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

class Bubbles(
    x: Int,
    y: Int,
    val shouldRender: () -> Boolean,
    val highTemperature: () -> Boolean
) : AbstractWidget(
    x, y,
    ScreenTextures.Elements.Bubbles.Dimensions.WIDTH,
    ScreenTextures.Elements.Bubbles.Dimensions.HEIGHT,
    Component.empty()
) {

    private var bubblePosProgress = 0
    private var bubblePos = 0
        set(value) {
            field = value

            val amountOverMax = bubblePos - ScreenTextures.Elements.Bubbles.Dimensions.HEIGHT
            if (amountOverMax > 0) {
                field = amountOverMax
            }
        }

    override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        if (!shouldRender()) return

        val bubbleSpeed = if (highTemperature()) {
            IncubatorScreen.FAST_BUBBLE_SPEED
        } else {
            AdvancedIncubatorScreen.SLOW_BUBBLE_SPEED
        }

        if (++bubblePosProgress % bubbleSpeed == 0) {
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
            x,
            y,
            ScreenTextures.Elements.Bubbles.Dimensions.WIDTH,
            amountBubbleToRender
        )
    }

    override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        return this.defaultButtonNarrationText(pNarrationElementOutput)
    }
}