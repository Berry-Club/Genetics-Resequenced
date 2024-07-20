package dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_infuser

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class PlasmidInfuserScreen(
    pMenu: PlasmidInfuserMenu,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<PlasmidInfuserMenu>(pMenu, pPlayerInventory, pTitle) {

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        pGuiGraphics.blit(
            ScreenTextures.Backgrounds.PLASMID_INFUSER,
            leftPos, topPos,
            0, 0,
            ScreenTextures.Backgrounds.TEXTURE_SIZE,
            ScreenTextures.Backgrounds.TEXTURE_SIZE
        )

        renderProgressArrow(pGuiGraphics, leftPos, topPos,)
        renderEnergyInfo(pGuiGraphics, leftPos, topPos,)
    }

    private fun renderEnergyInfo(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        val energyStorage = menu.blockEntity.energyStorage
        val percent = energyStorage.energyStored.toFloat() / energyStorage.maxEnergyStored.toFloat()

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Energy.TEXTURE,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            0,
            0,
            x + ScreenTextures.Elements.Energy.Location.Default.X,
            y + ScreenTextures.Elements.Energy.Location.Default.Y,
            0,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            (ScreenTextures.Elements.Energy.TEXTURE_SIZE * percent).toInt()
        )
    }

    override fun renderLabels(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
        val x = (width - imageWidth) / 2
        val y = (height - imageHeight) / 2
        renderEnergyAreaTooltip(pGuiGraphics, x, y, pMouseX, pMouseY)

        super.renderLabels(pGuiGraphics, pMouseX, pMouseY)
    }

    private fun renderEnergyAreaTooltip(pGuiGraphics: GuiGraphics, x: Int, y: Int, pMouseX: Int, pMouseY: Int) {
        if (isMouseOver(
                pMouseX, pMouseY,
                x, y,
                ScreenTextures.Elements.Energy.Location.Default.X,
                ScreenTextures.Elements.Energy.Location.Default.Y,
                ScreenTextures.Elements.Energy.Dimensions.WIDTH,
                ScreenTextures.Elements.Energy.Dimensions.HEIGHT
            )
        ) {
            val energyStorage = menu.blockEntity.energyStorage

            pGuiGraphics.renderComponentTooltip(
                Minecraft.getInstance().font,
                Component
                    .literal(energyStorage.energyStored.toString() + "/" + energyStorage.maxEnergyStored + " FE")
                    .toFlatList(),
                pMouseX - x,
                pMouseY - y
            )
        }

    }

    @Suppress("SameParameterValue")
    private fun isMouseOver(
        mouseX: Int, mouseY: Int,
        x: Int, y: Int,
        topLeftX: Int,
        topLeftY: Int,
        width: Int,
        height: Int
    ): Boolean {
        return MouseUtil.isMouseOver(
            mouseX, mouseY,
            x + topLeftX, y + topLeftY,
            width, height
        )
    }

    private fun renderProgressArrow(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!menu.isCrafting) return

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.ArrowRight.TEXTURE,
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE,
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE,
            0,
            0,
            x + ScreenTextures.Elements.ArrowRight.Position.Blood.X,
            y + ScreenTextures.Elements.ArrowRight.Position.Blood.Y,
            menu.getScaledProgress(),
            ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE
        )
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        super.renderTooltip(pGuiGraphics, pMouseX, pMouseY)
    }

}