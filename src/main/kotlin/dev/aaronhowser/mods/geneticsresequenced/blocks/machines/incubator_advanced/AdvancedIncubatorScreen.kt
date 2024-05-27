package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator_advanced

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.geneticsresequenced.screens.renderer.EnergyInfoArea
import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class AdvancedIncubatorScreen(
    pMenu: AdvancedIncubatorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<AdvancedIncubatorMenu>(pMenu, pPlayerInventory, pTitle) {

    companion object {
        val BACKGROUND_TEXTURE = OtherUtil.modResource("textures/gui/incubator.png")

        const val ARROW_TEXTURE_X = 177
        const val ARROW_TEXTURE_Y = 47
        const val ARROW_X = 101
        const val ARROW_Y = 14
        const val ARROW_WIDTH = 9
        const val ARROW_HEIGHT = 28

        const val BUBBLES_TEXTURE_X = 186
        const val BUBBLES_TEXTURE_Y = 47
        const val BUBBLES_X = 67
        const val BUBBLES_Y = 12
        const val BUBBLES_WIDTH = 12
        const val BUBBLES_HEIGHT = 29

        const val FULL_FUEL_TEXTURE_X = 177
        const val FULL_FUEL_TEXTURE_Y = 76
        const val EMPTY_FUEL_TEXTURE_X = 177
        const val EMPTY_FUEL_TEXTURE_Y = 81

        const val FUEL_X = 64
        const val FUEL_Y = 42
        const val FUEL_WIDTH = 18
        const val FUEL_HEIGHT = 4

        const val ENERGY_TEXTURE_X = 177
        const val ENERGY_TEXTURE_Y = 3
        const val ENERGY_X = 24
        const val ENERGY_Y = 14
        const val ENERGY_WIDTH = 14
        const val ENERGY_HEIGHT = 42

        private const val FAST_BUBBLE_SPEED = 4
    }

    private lateinit var energyInfoArea: EnergyInfoArea

    override fun init() {
        super.init()
        assignInfoArea()
    }

    private fun assignInfoArea() {
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2

        energyInfoArea = EnergyInfoArea(
            x + ENERGY_X,
            y + ENERGY_Y,
            menu.blockEntity.energyStorage,
            ENERGY_WIDTH,
            ENERGY_HEIGHT
        )

    }

    override fun renderBg(pPoseStack: PoseStack, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE)
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2

        blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight)

        renderFuel(pPoseStack, x, y)
        renderProgressArrow(pPoseStack, x, y)
        renderBubble(pPoseStack, x, y)

        energyInfoArea.draw(pPoseStack)
    }

    override fun renderLabels(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int) {

        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2

        renderEnergyAreaTooltip(pPoseStack, x, y, pMouseX, pMouseY)

        super.renderLabels(pPoseStack, pMouseX, pMouseY)
    }

    private fun renderEnergyAreaTooltip(pPoseStack: PoseStack, x: Int, y: Int, pMouseX: Int, pMouseY: Int) {

        if (isMouseOver(pMouseX, pMouseY, x, y, ENERGY_X, ENERGY_Y, ENERGY_WIDTH, ENERGY_HEIGHT)) {
            renderTooltip(pPoseStack, energyInfoArea.tooltip, pMouseX - x, pMouseY - y)
        }

    }

    private fun isMouseOver(
        mouseX: Int,
        mouseY: Int,
        x: Int,
        y: Int,
        offsetX: Int,
        offsetY: Int,
        width: Int,
        height: Int
    ): Boolean {
        return MouseUtil.isMouseOver(
            mouseX.toDouble(),
            mouseY.toDouble(),
            x + offsetX,
            y + offsetY,
            width,
            height
        )
    }

    //FIXME: Make it fill up instead of emptying
    private fun renderProgressArrow(pPoseStack: PoseStack, x: Int, y: Int) {
        if (!menu.isCrafting) return

        blit(
            pPoseStack,
            x + ARROW_X,
            y + ARROW_Y,
            ARROW_TEXTURE_X,
            ARROW_TEXTURE_Y,
            ARROW_WIDTH,
            menu.getScaledProgress()
        )

    }

    private var bubblePosProgress = 0
    private var bubblePos = 0
        set(value) {
            field = value

            val amountOverMax = bubblePos - BUBBLES_HEIGHT
            if (amountOverMax > 0) {
                field = amountOverMax
            }
        }

    //FIXME: Make the bubbles go up instead of down
    private fun renderBubble(pPoseStack: PoseStack, x: Int, y: Int) {
        if (!menu.isCrafting) return

        if (++bubblePosProgress % FAST_BUBBLE_SPEED == 0) {
            bubblePos++
            bubblePosProgress = 0
        }

        val amountBubbleToRender = BUBBLES_HEIGHT - bubblePos

        blit(
            pPoseStack,
            x + BUBBLES_X,
            y + BUBBLES_Y + (BUBBLES_HEIGHT - amountBubbleToRender),
            BUBBLES_TEXTURE_X,
            BUBBLES_TEXTURE_Y + (BUBBLES_HEIGHT - amountBubbleToRender),
            BUBBLES_WIDTH,
            amountBubbleToRender
        )
    }

    private fun renderFuel(pPoseStack: PoseStack, x: Int, y: Int) {
        val hasEnergy = menu.blockEntity.energyStorage.energyStored != 0

        blit(
            pPoseStack,
            x + FUEL_X,
            y + FUEL_Y,
            if (hasEnergy) FULL_FUEL_TEXTURE_X else EMPTY_FUEL_TEXTURE_X,
            if (hasEnergy) FULL_FUEL_TEXTURE_Y else EMPTY_FUEL_TEXTURE_Y,
            FUEL_WIDTH,
            FUEL_HEIGHT
        )

    }

    override fun render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderBackground(pPoseStack)
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
        renderTooltip(pPoseStack, pMouseX, pMouseY)
    }

}