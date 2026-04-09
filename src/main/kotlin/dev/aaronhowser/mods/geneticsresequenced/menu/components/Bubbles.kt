package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.menu.advanced_incubator.AdvancedIncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.incubator.IncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
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
	WIDTH,
	HEIGHT,
	Component.empty()
) {

	private var bubblePosProgress = 0
	private var bubblePos = 0
		set(value) {
			field = value

			val amountOverMax = bubblePos - HEIGHT
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

		val amountBubbleToRender = HEIGHT - bubblePos

		pGuiGraphics.blitSprite(
			TEXTURE,
			TEXTURE_SIZE,
			TEXTURE_SIZE,
			0,
			0,
			x,
			y,
			WIDTH,
			amountBubbleToRender
		)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		val TEXTURE = GeneticsResequenced.modResource("bubbles")
		const val TEXTURE_SIZE = 32

		const val WIDTH = 11
		const val HEIGHT = 29

		const val X = 67
		const val Y = 18
	}

}