package dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
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
    ScreenTextures.Elements.Heat.Dimensions.WIDTH,
    ScreenTextures.Elements.Heat.Dimensions.HEIGHT,
    Component.empty()
) {

    override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        if (!shouldRender()) return

        val texture = if (isHighTemperature()) {
            ScreenTextures.Elements.Heat.Texture.HIGH
        } else {
            ScreenTextures.Elements.Heat.Texture.LOW
        }

        pGuiGraphics.blitSprite(
            texture,
            ScreenTextures.Elements.Heat.TEXTURE_SIZE,
            ScreenTextures.Elements.Heat.TEXTURE_SIZE,
            0, 0,
            x,
            y,
            ScreenTextures.Elements.Heat.Dimensions.WIDTH,
            ScreenTextures.Elements.Heat.Dimensions.HEIGHT
        )

        if (isHovered) renderTooltip(pGuiGraphics, pMouseX, pMouseY)
    }

    private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        if (!shouldRenderTooltip) return

        pGuiGraphics.renderComponentTooltip(
            font,
            listOf(
                if (isHighTemperature())
                    ModLanguageProvider.Tooltips.INCUBATOR_SET_LOW.toComponent()
                else
                    ModLanguageProvider.Tooltips.INCUBATOR_SET_HIGH.toComponent()
            ),
            pMouseX, pMouseY
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