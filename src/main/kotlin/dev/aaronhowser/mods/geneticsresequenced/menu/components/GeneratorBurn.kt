package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
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
	WIDTH,
	HEIGHT,
	Component.empty()
) {

	override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {

		if (!shouldRender.asBoolean) return

		val totalHeight = HEIGHT
		val amountToRender = totalHeight - (totalHeight * percentDone.get()).toInt()

		pGuiGraphics.blit(
			TEXTURE,
			x,
			y + HEIGHT - amountToRender,
			0f,
			(HEIGHT - amountToRender).toFloat(),
			TEXTURE_SIZE,
			amountToRender,
			TEXTURE_SIZE,
			TEXTURE_SIZE
		)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		val TEXTURE = OtherUtil.modResource("burn")
		const val TEXTURE_SIZE = 16

		const val X = 52
		const val Y = 59

		const val WIDTH = 14
		const val HEIGHT = 14
	}

}