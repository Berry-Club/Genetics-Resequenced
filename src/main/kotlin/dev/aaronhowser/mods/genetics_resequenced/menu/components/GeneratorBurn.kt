package dev.aaronhowser.mods.genetics_resequenced.menu.components

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.renderer.RenderPipelines
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

	override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {

		if (!shouldRender.asBoolean) return

		val totalHeight = HEIGHT
		val amountToRender = totalHeight - (totalHeight * percentDone.get()).toInt()

		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			TEXTURE,
			TEXTURE_SIZE,
			TEXTURE_SIZE,
			0,
			HEIGHT - amountToRender,
			x,
			y + HEIGHT - amountToRender,
			TEXTURE_SIZE,
			amountToRender
		)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		val TEXTURE = GeneticsResequenced.modId("burn")
		const val TEXTURE_SIZE = 16

		const val X = 52
		const val Y = 59

		const val WIDTH = 14
		const val HEIGHT = 14
	}

}
