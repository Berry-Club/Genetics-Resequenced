package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineScreen
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part.ProgressArrow
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator.IncubatorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
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

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        super.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY)

        renderHeat(pGuiGraphics, leftPos, topPos)
        renderBubble(pGuiGraphics, leftPos, topPos)
    }

    override fun renderTooltip(guiGraphics: GuiGraphics, x: Int, y: Int) {
        super.renderTooltip(guiGraphics, x, y)

        showTemperatureTooltip(guiGraphics, x, y)
    }

    override fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int): Boolean {
        if (isMouseOverTemperature(pMouseX.toInt(), pMouseY.toInt(), leftPos, topPos)) {
            this.minecraft?.gameMode?.handleInventoryButtonClick(this.menu.containerId, 1)
            bubblePosProgress = 0
            bubblePos = 0
            return true
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton)
    }

    private fun isMouseOverTemperature(mouseX: Int, mouseY: Int, x: Int, y: Int): Boolean {
        return isMouseOver(
            mouseX, mouseY,
            x, y,
            ScreenTextures.Elements.Heat.Position.X,
            ScreenTextures.Elements.Heat.Position.Y,
            ScreenTextures.Elements.Heat.Dimensions.WIDTH,
            ScreenTextures.Elements.Heat.Dimensions.HEIGHT
        )
    }

    private fun showTemperatureTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        if (!isMouseOverTemperature(pMouseX, pMouseY, leftPos, topPos)) return

        pGuiGraphics.renderComponentTooltip(
            font,
            listOf(
                if (menu.isHighTemperature)
                    ModLanguageProvider.Tooltips.INCUBATOR_SET_LOW.toComponent()
                else
                    ModLanguageProvider.Tooltips.INCUBATOR_SET_HIGH.toComponent()
            ),
            pMouseX, pMouseY
        )
    }

    override val energyPosLeft: Int = ScreenTextures.Elements.Energy.Location.Incubator.X
    override val energyPosTop: Int = ScreenTextures.Elements.Energy.Location.Incubator.Y
    override val arrowDirection: ProgressArrow.ArrowDirection = ProgressArrow.ArrowDirection.DOWN

    override val arrowLeftPos: Int = ScreenTextures.Elements.ArrowDown.Position.X
    override val arrowTopPos: Int = ScreenTextures.Elements.ArrowDown.Position.Y

    override fun getArrowTooltip(): List<Component> {
        val blockEntity = menu.blockEntity as? AdvancedIncubatorBlockEntity ?: return emptyList()
        if (!blockEntity.isBrewing) return emptyList()

        val ticksRemaining = blockEntity.ticksRemaining
        val ticksPerBrew = IncubatorBlockEntity.ticksPerBrew

        val percentLeft = (ticksRemaining.toFloat() / ticksPerBrew.toFloat())
        val percentDone = 1 - percentLeft

        val percentString = (percentDone * 100).toInt().toString() + "%"

        return listOf(
            Component.literal(percentString)
        )
    }

    override fun progressArrowAmountToRender(): Int = menu.getScaledProgress()
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

    private fun renderHeat(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        val hasEnergy = menu.blockEntity.energyStorage.energyStored != 0
        if (!hasEnergy) return

        val texture =
            if (menu.isHighTemperature) ScreenTextures.Elements.Heat.Texture.HIGH else ScreenTextures.Elements.Heat.Texture.LOW

        pGuiGraphics.blitSprite(
            texture,
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