package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.menu.ScreenTextures
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import java.util.function.IntSupplier

class EnergyBar(
	x: Int,
	y: Int,
	val maxGetter: IntSupplier,
	val currentGetter: IntSupplier,
	val font: Font
) : AbstractWidget(
	x, y,
	ScreenTextures.Sprites.ENERGY.width,
	ScreenTextures.Sprites.ENERGY.height,
	Component.empty()
) {

	override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
		val percentFull = currentGetter.asInt.toFloat() / maxGetter.asInt.toFloat()

		val energyTotalHeight = this.height
		val energyCurrentHeight = Mth.ceil(energyTotalHeight.toDouble() * percentFull)

		pGuiGraphics.blit(
			ScreenTextures.Sprites.ENERGY.texture,
			x,
			y + energyTotalHeight - energyCurrentHeight,
			0f,
			(energyTotalHeight - energyCurrentHeight).toFloat(),
			TEXTURE_SIZE,
			energyCurrentHeight,
			TEXTURE_SIZE,
			TEXTURE_SIZE
		)

		if (isHovered) renderTooltip(pGuiGraphics, pMouseX, pMouseY)
	}

	private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
		val currentAmountString = String.format("%,d", currentGetter.asInt)
		val maxAmountString = String.format("%,d", maxGetter.asInt)

		val component = ModTooltipLang.FE.toComponent(currentAmountString, maxAmountString)

		pGuiGraphics.renderComponentTooltip(
			font,
			listOf(component),
			pMouseX,
			pMouseY
		)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		const val TEXTURE_SIZE = 64
	}

}