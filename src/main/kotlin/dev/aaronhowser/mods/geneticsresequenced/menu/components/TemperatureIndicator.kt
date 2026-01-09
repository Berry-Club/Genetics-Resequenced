package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

class TemperatureIndicator(
	x: Int,
	y: Int,
	val font: Font,
	val shouldRender: () -> Boolean,
	val shouldRenderTooltip: Boolean,
	val isHighTemperature: () -> Boolean,
	val onClickFunction: (Double, Double) -> Unit
) : AbstractWidget(
	x, y,
	ScreenTextures.Sprites.HEAT_HIGH.width,
	ScreenTextures.Sprites.HEAT_HIGH.height,
	Component.empty()
) {

	override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
		if (!shouldRender()) return

		val sprite = if (isHighTemperature()) {
			ScreenTextures.Sprites.HEAT_HIGH
		} else {
			ScreenTextures.Sprites.HEAT_LOW
		}

		pGuiGraphics.blit(
			sprite.texture,

			x,
			y,

			sprite.uStart.toFloat(),
			sprite.vStart.toFloat(),

			sprite.width,
			sprite.height,

			ScreenTextures.Sprites.SPRITE_SHEET_SIZE,
			ScreenTextures.Sprites.SPRITE_SHEET_SIZE
		)

		if (isHovered) renderTooltip(pGuiGraphics, pMouseX, pMouseY)
	}

	private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
		if (!shouldRenderTooltip) return

		pGuiGraphics.renderComponentTooltip(
			font,
			listOf(
				if (isHighTemperature())
					ModTooltipLang.INCUBATOR_SET_LOW.toComponent()
				else
					ModTooltipLang.INCUBATOR_SET_HIGH.toComponent()
			),
			pMouseX, pMouseY
		)
	}

	override fun onClick(mouseX: Double, mouseY: Double) {
		super.onClick(mouseX, mouseY)

		onClickFunction(mouseX, mouseY)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		const val X = 64
		const val Y = 48
	}
}