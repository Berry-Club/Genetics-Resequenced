package dev.aaronhowser.mods.geneticsresequenced.menu.components

import dev.aaronhowser.mods.geneticsresequenced.block_old.base.handler.ModEnergyStorage
import dev.aaronhowser.mods.geneticsresequenced.block_old.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth

class EnergyBar(
	x: Int,
	y: Int,
	val energyStorage: ModEnergyStorage,
	val font: Font
) : AbstractWidget(
	x, y,
	ScreenTextures.Elements.Energy.Dimensions.WIDTH,
	ScreenTextures.Elements.Energy.Dimensions.HEIGHT,
	Component.empty()
) {

	override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
		val percentFull = energyStorage.energyStored.toFloat() / energyStorage.maxEnergyStored.toFloat()

		val energyTotalHeight = this.height
		val energyCurrentHeight = Mth.ceil(energyTotalHeight.toDouble() * percentFull)

		pGuiGraphics.blitSprite(
			ScreenTextures.Elements.Energy.TEXTURE,
			ScreenTextures.Elements.Energy.TEXTURE_SIZE,
			ScreenTextures.Elements.Energy.TEXTURE_SIZE,
			0,
			energyTotalHeight - energyCurrentHeight,
			x,
			y + energyTotalHeight - energyCurrentHeight,
			ScreenTextures.Elements.Energy.TEXTURE_SIZE,
			energyCurrentHeight
		)

		if (isHovered) renderTooltip(pGuiGraphics, pMouseX, pMouseY)
	}

	private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
		val currentAmountString = String.format("%,d", energyStorage.energyStored)
		val maxAmountString = String.format("%,d", energyStorage.maxEnergyStored)

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

}