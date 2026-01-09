package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import java.util.function.BooleanSupplier
import java.util.function.Supplier

class GeneratorBurn(
	x: Int,
	y: Int,
	val shouldRender: BooleanSupplier,
	val percentDone: Supplier<Float>,
) : AbstractWidget(
	x, y,
	ScreenTextures.Sprites.BURN.width,
	ScreenTextures.Sprites.BURN.height,
	Component.empty()
) {

	override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
		if (!shouldRender.asBoolean) return

		val sprite = ScreenTextures.Sprites.BURN

		val totalHeight = sprite.height
		val amountToRender = totalHeight - (totalHeight * percentDone.get()).toInt()

		val cutoff = sprite.height - amountToRender

		pGuiGraphics.blit(
			sprite.texture,

			x,
			y + cutoff,

			sprite.uStart.toFloat(),
			sprite.vStart.toFloat() + cutoff,

			sprite.width,
			amountToRender,

			ScreenTextures.Sprites.SPRITE_SHEET_SIZE,
			ScreenTextures.Sprites.SPRITE_SHEET_SIZE
		)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		const val X = 52
		const val Y = 59
	}

}