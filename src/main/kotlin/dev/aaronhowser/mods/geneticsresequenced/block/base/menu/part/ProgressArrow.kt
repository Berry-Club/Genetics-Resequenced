package dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class ProgressArrow(
    x: Int,
    y: Int,
    val arrowDirection: ArrowDirection,
    val font: Font,
    val tooltipComponentsFunction: () -> List<Component>,
    val amountFunction: () -> Int,
    val shouldRenderProgress: () -> Boolean,
    val onClickFunction: (Double, Double, Int) -> Unit
) : AbstractWidget(
    x, y,
    arrowDirection.width,
    arrowDirection.height,
    Component.empty()
) {

    enum class ArrowDirection(
        val width: Int,
        val height: Int,
        val texture: ResourceLocation,
        val textureSize: Int
    ) {
        DOWN(
            ScreenTextures.Elements.ArrowDown.Dimensions.WIDTH,
            ScreenTextures.Elements.ArrowDown.Dimensions.HEIGHT,
            ScreenTextures.Elements.ArrowDown.TEXTURE,
            ScreenTextures.Elements.ArrowDown.TEXTURE_SIZE
        ),
        RIGHT(
            ScreenTextures.Elements.ArrowRight.Dimensions.WIDTH,
            ScreenTextures.Elements.ArrowRight.Dimensions.HEIGHT,
            ScreenTextures.Elements.ArrowRight.TEXTURE,
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE
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
            amountFunction(),
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
            amountFunction(),
            this.height
        )
    }

    private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        pGuiGraphics.renderComponentTooltip(
            font,
            tooltipComponentsFunction(),
            pMouseX,
            pMouseY
        )
    }

    override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        return this.defaultButtonNarrationText(pNarrationElementOutput)
    }

    override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
        super.onClick(mouseX, mouseY, button)

        onClickFunction(mouseX, mouseY, button)
    }

}