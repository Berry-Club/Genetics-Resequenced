package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.menu.advanced_incubator.AdvancedIncubatorScreen
import dev.aaronhowser.mods.geneticsresequenced.menu.incubator.IncubatorScreen
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
	ScreenTextures.Sprites.BUBBLES.width,
	ScreenTextures.Sprites.BUBBLES.height,
	Component.empty()
) {

	private var bubblePosProgress = 0
	private var bubblePos = 0
		set(value) {
			field = value

			val amountOverMax = bubblePos - ScreenTextures.Sprites.BUBBLES.height
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

		val sprite = ScreenTextures.Sprites.BUBBLES

		val amountBubbleToRender = sprite.height - bubblePos

		pGuiGraphics.blit(
			sprite.texture,

			x,
			y,

			sprite.uStart.toFloat(),
			sprite.vStart.toFloat(),

			sprite.width,
			amountBubbleToRender,

			ScreenTextures.Sprites.SPRITE_SHEET_SIZE,
			ScreenTextures.Sprites.SPRITE_SHEET_SIZE
		)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		const val X = 67
		const val Y = 18
	}

}