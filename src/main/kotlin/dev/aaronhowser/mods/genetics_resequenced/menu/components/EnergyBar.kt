package dev.aaronhowser.mods.genetics_resequenced.menu.components

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.renderer.RenderPipelines
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
	WIDTH,
	HEIGHT,
	Component.empty()
) {

	override fun extractWidgetRenderState(graphics: GuiGraphicsExtractor, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
		val percentFull = currentGetter.asInt.toFloat() / maxGetter.asInt.toFloat()

		val energyTotalHeight = this.height
		val energyCurrentHeight = Mth.ceil(energyTotalHeight.toDouble() * percentFull)

		graphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			TEXTURE,
			TEXTURE_SIZE,
			TEXTURE_SIZE,
			0,
			energyTotalHeight - energyCurrentHeight,
			x,
			y + energyTotalHeight - energyCurrentHeight,
			TEXTURE_SIZE,
			energyCurrentHeight
		)

		if (isHovered) renderTooltip(graphics, pMouseX, pMouseY)
	}

	private fun renderTooltip(graphics: GuiGraphicsExtractor, pMouseX: Int, pMouseY: Int) {
		val currentAmountString = String.format("%,d", currentGetter.asInt)
		val maxAmountString = String.format("%,d", maxGetter.asInt)

		val component = ModTooltipLang.FE.toComponent(currentAmountString, maxAmountString)

		graphics.setComponentTooltipForNextFrame(
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
		const val WIDTH = 18
		const val HEIGHT = 57
		const val TEXTURE_SIZE = 64
		val TEXTURE = GeneticsResequenced.modResource("energy")
	}

}
