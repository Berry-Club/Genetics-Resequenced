package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part.ProgressArrow
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part.TemperatureArea
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class AdvancedIncubatorScreen(
    pMenu: AdvancedIncubatorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : MachineScreen<AdvancedIncubatorMenu>(pMenu, pPlayerInventory, pTitle) {

    companion object {
        private const val FAST_BUBBLE_SPEED = 12
        private const val SLOW_BUBBLE_SPEED = 12 * 3
    }

    override val backgroundTexture: ResourceLocation = ScreenTextures.Backgrounds.INCUBATOR_ADVANCED

    private lateinit var temperatureArea: TemperatureArea

    override fun init() {
        super.init()

        temperatureArea = TemperatureArea(
            x = leftPos + ScreenTextures.Elements.Heat.Position.X,
            y = topPos + ScreenTextures.Elements.Heat.Position.Y,
            font = font,
            shouldRender = { menu.blockEntity.energyStorage.energyStored != 0 },
            shouldRenderTooltip = true,
            isHighTemperature = { menu.isHighTemperature },
            onClickFunction = { _, _, _ ->
                this.minecraft?.gameMode?.handleInventoryButtonClick(this.menu.containerId, 1)
                bubblePosProgress = 0
                bubblePos = 0
            }
        )

        addRenderableWidget(temperatureArea)
    }

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY)

        renderBubble(pGuiGraphics, leftPos, topPos)
    }

    override val energyPosLeft: Int = ScreenTextures.Elements.Energy.Location.Incubator.X
    override val energyPosTop: Int = ScreenTextures.Elements.Energy.Location.Incubator.Y
    override val arrowDirection: ProgressArrow.ArrowDirection = ProgressArrow.ArrowDirection.DOWN

    override val arrowLeftPos: Int = ScreenTextures.Elements.ArrowDown.Position.X
    override val arrowTopPos: Int = ScreenTextures.Elements.ArrowDown.Position.Y

    override fun arrowPercentDone(): Float = menu.getPercentDone()
    override fun shouldRenderProgressArrow(): Boolean = menu.isCrafting

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

        val speed = if (menu.isHighTemperature) FAST_BUBBLE_SPEED else SLOW_BUBBLE_SPEED

        bubblePosProgress++
        if (bubblePosProgress % speed == 0) {
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
}