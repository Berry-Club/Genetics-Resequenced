package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.geneticsresequenced.screens.renderer.EnergyInfoArea
import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class CoalGeneratorScreen(
    pMenu: CoalGeneratorMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<CoalGeneratorMenu>(pMenu, pPlayerInventory, pTitle) {

    companion object {
        val BACKGROUND_TEXTURE = OtherUtil.modResource("textures/gui/coal_generator.png")

        const val ARROW_TEXTURE_X = 177
        const val ARROW_TEXTURE_Y = 61
        const val ARROW_X = 75
        const val ARROW_Y = 35
        const val ARROW_WIDTH = 24
        const val ARROW_HEIGHT = 17

        const val BURN_TEXTURE_X = 177
        const val BURN_TEXTURE_Y = 46
        const val BURN_X = 53
        const val BURN_Y = 54
        const val BURN_WIDTH = 14
        const val BURN_HEIGHT = 14

        const val ENERGY_TEXTURE_X = 177
        const val ENERGY_TEXTURE_Y = 3
        const val ENERGY_X = 104
        const val ENERGY_Y = 20
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

        renderProgressArrow(pPoseStack, x, y)
        renderBurnProgress(pPoseStack, x, y)
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

    private fun renderProgressArrow(pPoseStack: PoseStack, x: Int, y: Int) {
        if (menu.isBurning) {
            blit(
                pPoseStack,
                x + ARROW_X,                // The x position of where the arrow will be
                y + ARROW_Y,                // The y position of where the arrow will be
                ARROW_TEXTURE_X,                // The x offset of where the arrow is in the texture
                ARROW_TEXTURE_Y,                // The y offset of where the arrow is in the texture
                menu.getScaledProgressArrow(),       // The width of the arrow
                ARROW_HEIGHT                    // The height of the arrow
            )
        }
    }

    //FIXME: The bottom disappears rather than the top
    private fun renderBurnProgress(pPoseStack: PoseStack, x: Int, y: Int) {
        if (menu.isBurning) {
            blit(
                pPoseStack,
                x + BURN_X,
                y + BURN_Y,
                BURN_TEXTURE_X,
                BURN_TEXTURE_Y,
                BURN_WIDTH,
                menu.getScaledFuelRemaining()
            )
        }
    }

    override fun render(pPoseStack: PoseStack, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderBackground(pPoseStack)
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick)
        renderTooltip(pPoseStack, pMouseX, pMouseY)
    }

}