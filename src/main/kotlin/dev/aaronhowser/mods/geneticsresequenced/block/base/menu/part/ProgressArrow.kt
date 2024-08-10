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
    val amountFunction: () -> Int,
    val shouldRenderProgress: () -> Boolean,
    message: Component = Component.empty()
) : AbstractWidget(
    x, y,
    arrowDirection.width,
    arrowDirection.height,
    message
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
            renderDownArrow(pGuiGraphics, pMouseX, pMouseY)
        } else {
            renderRightArrow(pGuiGraphics, pMouseX, pMouseY)
        }

        if (isHovered) renderTooltip(pGuiGraphics, pMouseX, pMouseY)

    }

    private fun renderDownArrow(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        if (!shouldRenderProgress()) return

        pGuiGraphics.blitSprite(
            arrowDirection.texture,
            arrowDirection.textureSize, arrowDirection.textureSize,
            0, 0,
            this.x,
            this.y,
            width,
            amountFunction(),
        )
    }

    private fun renderRightArrow(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        if (!shouldRenderProgress()) return

        pGuiGraphics.blitSprite(
            arrowDirection.texture,
            arrowDirection.textureSize, arrowDirection.textureSize,
            0, 0,
            this.x,
            this.y,
            this.width,
            this.height
        )
    }

    private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        pGuiGraphics.renderComponentTooltip(
            font,
            listOf(Component.literal("Hi!")),
            pMouseX, pMouseY
        )
    }

    override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
        return this.defaultButtonNarrationText(pNarrationElementOutput)
    }


}