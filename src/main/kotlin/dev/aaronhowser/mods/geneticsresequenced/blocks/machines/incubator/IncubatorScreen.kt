package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.geneticsresequenced.screens.renderer.EnergyInfoArea
import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class IncubatorScreen(
    pMenu: IncubatorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<IncubatorMenu>(pMenu, pPlayerInventory, pTitle) {

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


    override fun render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderBackground(pPoseStack)
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
        renderTooltip(pPoseStack, pMouseX, pMouseY)
    }

}