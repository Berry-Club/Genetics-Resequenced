package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class IncubatorScreen(
    pMenu: IncubatorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<IncubatorMenu>(pMenu, pPlayerInventory, pTitle) {

    companion object {
        private const val FAST_BUBBLE_SPEED = 12
    }

    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.INCUBATOR

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY)

        renderHeat(pGuiGraphics, leftPos, topPos)
        renderBubble(pGuiGraphics, leftPos, topPos)
    }

    override val energyPosLeft: Int = ScreenTextures.Elements.Energy.Location.Incubator.X
    override val energyPosTop: Int = ScreenTextures.Elements.Energy.Location.Incubator.Y

    override val arrowTexture: ResourceLocation = ScreenTextures.Elements.ArrowDown.TEXTURE
    override val arrowTextureSize: Int = ScreenTextures.Elements.ArrowDown.TEXTURE_SIZE
    override val arrowLeftPos: Int = ScreenTextures.Elements.ArrowDown.Position.X
    override val arrowTopPos: Int = ScreenTextures.Elements.ArrowDown.Position.Y
    override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting
    override fun progressArrowWidth(): Int = ScreenTextures.Elements.ArrowDown.Dimensions.WIDTH
    override fun progressArrowHeight(): Int = menu.getScaledProgress()

    override fun renderProgressArrow(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!shouldRenderProgressArrow()) return

        val height = progressArrowHeight()

        pGuiGraphics.blitSprite(
            arrowTexture,
            arrowTextureSize,
            arrowTextureSize,
            0,
            0,
            x + arrowLeftPos,
            y + arrowTopPos,
            progressArrowWidth(),
            height
        )
    }

    private var bubblePosProgress = 0
    private var bubblePos = 0
        set(value) {
            field = value

            val amountOverMax = bubblePos - ScreenTextures.Elements.Bubbles.Dimensions.HEIGHT
            if (amountOverMax > 0) {
                field = amountOverMax
            }
        }

    //FIXME: Make the bubbles go up instead of down
    private fun renderBubble(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!menu.isCrafting) return

        if (++bubblePosProgress % FAST_BUBBLE_SPEED == 0) {
            bubblePos++
            bubblePosProgress = 0
        }

        val amountBubbleToRender = ScreenTextures.Elements.Bubbles.Dimensions.HEIGHT - bubblePos

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Bubbles.TEXTURE,
            ScreenTextures.Elements.Bubbles.TEXTURE_SIZE,
            ScreenTextures.Elements.Bubbles.TEXTURE_SIZE,
            0,
            0,
            x + ScreenTextures.Elements.Bubbles.Position.X,
            y + ScreenTextures.Elements.Bubbles.Position.Y,
            ScreenTextures.Elements.Bubbles.Dimensions.WIDTH,
            amountBubbleToRender
        )
    }

    private fun renderHeat(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        val hasEnergy = menu.blockEntity.energyStorage.energyStored != 0
        if (!hasEnergy) return

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Heat.Texture.HIGH,
            ScreenTextures.Elements.Heat.TEXTURE_SIZE,
            ScreenTextures.Elements.Heat.TEXTURE_SIZE,
            0, 0,
            x + ScreenTextures.Elements.Heat.Position.X,
            y + ScreenTextures.Elements.Heat.Position.Y,
            ScreenTextures.Elements.Heat.Dimensions.WIDTH,
            ScreenTextures.Elements.Heat.Dimensions.HEIGHT
        )
    }

}