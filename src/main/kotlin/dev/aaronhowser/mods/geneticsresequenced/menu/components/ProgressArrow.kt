package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth

class ProgressArrow(
	x: Int,
	y: Int,
	val arrowDirection: ArrowDirection,
	val font: Font,
	val percentDoneFunction: () -> Float,
	val shouldRenderProgress: () -> Boolean,
	val onClickFunction: (Double, Double) -> Unit
) : AbstractWidget(
	x, y,
	arrowDirection.sprite.width,
	arrowDirection.sprite.height,
	Component.empty()
) {

	enum class ArrowDirection(
		val sprite: ScreenTextures.Sprites.SpriteSheetFragment
	) {
		DOWN(ScreenTextures.Sprites.ARROW_DOWN),
		RIGHT(ScreenTextures.Sprites.ARROW_RIGHT)
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

		val height = Mth.floor(arrowDirection.sprite.height * percentDoneFunction())

		pGuiGraphics.blit(
			arrowDirection.sprite.texture,

			x,
			y,

			arrowDirection.sprite.uStart.toFloat(),
			arrowDirection.sprite.vStart.toFloat(),

			arrowDirection.sprite.width,
			height,

			ScreenTextures.Sprites.SPRITE_SHEET_SIZE,
			ScreenTextures.Sprites.SPRITE_SHEET_SIZE
		)
	}

	private fun renderRightArrow(pGuiGraphics: GuiGraphics) {
		if (!shouldRenderProgress()) return

		val width = Mth.floor(arrowDirection.sprite.width * percentDoneFunction())

		pGuiGraphics.blit(
			arrowDirection.sprite.texture,

			x,
			y,

			arrowDirection.sprite.uStart.toFloat(),
			arrowDirection.sprite.vStart.toFloat(),

			width,
			arrowDirection.sprite.height,

			ScreenTextures.Sprites.SPRITE_SHEET_SIZE,
			ScreenTextures.Sprites.SPRITE_SHEET_SIZE
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

	override fun onClick(mouseX: Double, mouseY: Double) {
		super.onClick(mouseX, mouseY)

		onClickFunction(mouseX, mouseY)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

}