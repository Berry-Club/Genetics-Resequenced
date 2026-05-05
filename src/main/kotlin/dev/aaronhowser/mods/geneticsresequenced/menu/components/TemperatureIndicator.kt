package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
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
	val onClickFunction: (Double, Double, Int) -> Unit
) : AbstractWidget(
	x, y,
	WIDTH,
	HEIGHT,
	Component.empty()
) {

	override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
		if (!shouldRender()) return

		val texture = if (isHighTemperature()) {
			HIGH
		} else {
			LOW
		}

		pGuiGraphics.blitSprite(
			texture,
			TEXTURE_SIZE,
			TEXTURE_SIZE,
			0, 0,
			x,
			y,
			WIDTH,
			HEIGHT
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

	override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
		super.onClick(mouseX, mouseY, button)

		onClickFunction(mouseX, mouseY, button)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		val HIGH = GeneticsResequenced.modResource("heat_high")
		val LOW = GeneticsResequenced.modResource("heat_low")

		const val TEXTURE_SIZE = 32

		const val WIDTH = 18
		const val HEIGHT = 4

		const val X = 64
		const val Y = 48
	}
}