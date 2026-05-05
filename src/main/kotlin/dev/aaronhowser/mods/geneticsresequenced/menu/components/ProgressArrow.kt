package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
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
		val texture: ResourceLocation,
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

	override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {

		if (arrowDirection == ArrowDirection.DOWN) {
			renderDownArrow(pGuiGraphics)
		} else {
			renderRightArrow(pGuiGraphics)
		}

		if (isHovered) renderTooltip(pGuiGraphics, pMouseX, pMouseY)
	}

	private fun renderDownArrow(pGuiGraphics: GuiGraphics) {
		if (!shouldRenderProgress()) return

		pGuiGraphics.blitSprite(
			arrowDirection.texture,
			arrowDirection.textureSize, arrowDirection.textureSize,
			0, 0,
			this.x,
			this.y,
			this.width,
			Mth.floor(this.height * percentDoneFunction()),
		)
	}

	private fun renderRightArrow(pGuiGraphics: GuiGraphics) {
		if (!shouldRenderProgress()) return

		pGuiGraphics.blitSprite(
			arrowDirection.texture,
			arrowDirection.textureSize, arrowDirection.textureSize,
			0, 0,
			this.x,
			this.y,
			Mth.floor(this.width * percentDoneFunction()),
			this.height
		)
	}

	private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
		if (percentDoneFunction() <= 0f) return

		val percentString = (percentDoneFunction() * 100).toInt().toString() + "%"

		pGuiGraphics.renderComponentTooltip(
			font,
			listOf(Component.literal(percentString)),
			pMouseX,
			pMouseY
		)
	}

	override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
		super.onClick(mouseX, mouseY, button)

		onClickFunction(mouseX, mouseY, button)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

}