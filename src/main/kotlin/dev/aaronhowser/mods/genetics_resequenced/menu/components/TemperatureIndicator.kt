package dev.aaronhowser.mods.genetics_resequenced.menu.components

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.client.renderer.RenderPipelines
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

	override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
		if (!shouldRender()) return

		val texture = if (isHighTemperature()) {
			HIGH
		} else {
			LOW
		}

		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			texture,
			TEXTURE_SIZE,
			TEXTURE_SIZE,
			0, 0,
			x,
			y,
			WIDTH,
			HEIGHT
		)

		if (isHovered) renderTooltip(graphics, pMouseX, pMouseY)
	}

	private fun renderTooltip(graphics: GuiGraphicsExtractor, pMouseX: Int, pMouseY: Int) {
		if (!shouldRenderTooltip) return

		graphics.setComponentTooltipForNextFrame(
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

	override fun onClick(event: MouseButtonEvent, doubleClick: Boolean) {
		super.onClick(event, doubleClick)

		onClickFunction(event.x(), event.y(), event.button())
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
