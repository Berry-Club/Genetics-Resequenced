package dev.aaronhowser.mods.genetics_resequenced.menu.components

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth

class ProgressArrow(
	x: Int,
	y: Int,
	val arrowDirection: ArrowDirection,
	val font: Font,
	val percentDoneFunction: () -> Float,
	val shouldRenderProgress: () -> Boolean,
	val onClickFunction: (Double, Double, Int) -> Unit
) : AbstractWidget(
	x, y,
	arrowDirection.width,
	arrowDirection.height,
	Component.empty()
) {

	companion object {
		const val TEXTURE_SIZE = 32

		val RIGHT_TEXTURE = GeneticsResequenced.modResource("arrow_right")
		const val RIGHT_WIDTH = 24
		const val RIGHT_HEIGHT = 17

		val DOWN_TEXTURE = GeneticsResequenced.modResource("arrow_down")
		const val DOWN_WIDTH = 9
		const val DOWN_HEIGHT = 28
	}

	enum class ArrowDirection(
		val width: Int,
		val height: Int,
		val texture: Identifier,
		val textureSize: Int
	) {
		DOWN(
			DOWN_WIDTH,
			DOWN_HEIGHT,
			DOWN_TEXTURE,
			TEXTURE_SIZE
		),
		RIGHT(
			RIGHT_WIDTH,
			RIGHT_HEIGHT,
			RIGHT_TEXTURE,
			TEXTURE_SIZE
		)
	}

	override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {

		if (arrowDirection == ArrowDirection.DOWN) {
			renderDownArrow(graphics)
		} else {
			renderRightArrow(graphics)
		}

		if (isHovered) renderTooltip(graphics, pMouseX, pMouseY)
	}

	private fun renderDownArrow(graphics: GuiGraphicsExtractor) {
		if (!shouldRenderProgress()) return

		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			arrowDirection.texture,
			arrowDirection.textureSize, arrowDirection.textureSize,
			0, 0,
			this.x,
			this.y,
			this.width,
			Mth.floor(this.height * percentDoneFunction()),
		)
	}

	private fun renderRightArrow(graphics: GuiGraphicsExtractor) {
		if (!shouldRenderProgress()) return

		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			arrowDirection.texture,
			arrowDirection.textureSize, arrowDirection.textureSize,
			0, 0,
			this.x,
			this.y,
			Mth.floor(this.width * percentDoneFunction()),
			this.height
		)
	}

	private fun renderTooltip(graphics: GuiGraphicsExtractor, pMouseX: Int, pMouseY: Int) {
		if (percentDoneFunction() <= 0f) return

		val percentString = (percentDoneFunction() * 100).toInt().toString() + "%"

		graphics.setComponentTooltipForNextFrame(
			font,
			listOf(Component.literal(percentString)),
			pMouseX,
			pMouseY
		)
	}

	override fun onClick(event: MouseButtonEvent, doubleClick: Boolean) {
		super.onClick(event, doubleClick)

		onClickFunction(event.x(), event.y(), event.button())
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

}
