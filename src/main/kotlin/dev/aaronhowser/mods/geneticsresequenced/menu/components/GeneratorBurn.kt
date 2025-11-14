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
	ScreenTextures.Elements.Burn.Dimensions.WIDTH,
	ScreenTextures.Elements.Burn.Dimensions.HEIGHT,
	Component.empty()
) {
	override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {

		if (!shouldRender.asBoolean) return

		val totalHeight = ScreenTextures.Elements.Burn.Dimensions.HEIGHT
		val amountToRender = totalHeight - (totalHeight * percentDone.get()).toInt()

		pGuiGraphics.blitSprite(
			ScreenTextures.Elements.Burn.TEXTURE,
			ScreenTextures.Elements.Burn.TEXTURE_SIZE,
			ScreenTextures.Elements.Burn.TEXTURE_SIZE,
			0,
			ScreenTextures.Elements.Burn.Dimensions.HEIGHT - amountToRender,
			x,
			y + ScreenTextures.Elements.Burn.Dimensions.HEIGHT - amountToRender,
			ScreenTextures.Elements.Burn.TEXTURE_SIZE,
			amountToRender
		)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}
}