package dev.aaronhowser.mods.geneticsresequenced.block.base.menu

import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

abstract class MachineScreen<T : MachineMenu>(
    pMenu: T,
    pPlayerInventory: Inventory,
    pTitle: Component
) : AbstractContainerScreen<T>(pMenu, pPlayerInventory, pTitle) {

    protected abstract val backgroundTexture: ResourceLocation
    protected abstract val backgroundSize: Int

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)

        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderBg(pGuiGraphics: GuiGraphics, pPartialTick: Float, pMouseX: Int, pMouseY: Int) {
        pGuiGraphics.blit(
            backgroundTexture,
            leftPos, topPos,
            0, 0,
            backgroundSize,
            backgroundSize
        )

        renderProgressArrow(pGuiGraphics, leftPos, topPos)
        renderEnergyInfo(pGuiGraphics, leftPos, topPos)
    }

    override fun renderTooltip(guiGraphics: GuiGraphics, x: Int, y: Int) {
        renderEnergyAreaTooltip(guiGraphics, leftPos, topPos, x, y)

        super.renderTooltip(guiGraphics, x, y)
    }

    // Energy Area

    protected abstract val energyX: Int
    protected abstract val energyY: Int
    protected open fun renderEnergyInfo(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        val energyStorage = menu.blockEntity.energyStorage
        val percent = energyStorage.energyStored.toFloat() / energyStorage.maxEnergyStored.toFloat()

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Energy.TEXTURE,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            0,
            0,
            x + energyX,
            y + energyY,
            0,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            (ScreenTextures.Elements.Energy.TEXTURE_SIZE * percent).toInt()
        )
    }

    protected open fun renderEnergyAreaTooltip(pGuiGraphics: GuiGraphics, x: Int, y: Int, pMouseX: Int, pMouseY: Int) {
        if (!isMouseOver(
                pMouseX, pMouseY,
                x, y,
                energyX,
                energyY,
                ScreenTextures.Elements.Energy.Dimensions.WIDTH,
                ScreenTextures.Elements.Energy.Dimensions.HEIGHT
            )
        ) return

        val energyStorage = menu.blockEntity.energyStorage

        pGuiGraphics.renderComponentTooltip(
            Minecraft.getInstance().font,
            Component
                .literal(energyStorage.energyStored.toString() + "/" + energyStorage.maxEnergyStored + " FE")
                .toFlatList(),
            pMouseX,
            pMouseY
        )
    }

    // Progress arrow
    protected open val arrowTexture: ResourceLocation = ScreenTextures.Elements.ArrowRight.TEXTURE
    protected open val arrowTextureSize: Int = ScreenTextures.Elements.ArrowRight.TEXTURE_SIZE
    protected abstract val arrowX: Int
    protected abstract val arrowY: Int
    protected abstract fun shouldRenderProgressArrow(): Boolean
    protected abstract fun progressArrowX(): Int
    protected abstract fun progressArrowY(): Int

    protected open fun renderProgressArrow(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!shouldRenderProgressArrow()) return

        pGuiGraphics.blitSprite(
            arrowTexture,
            arrowTextureSize,
            arrowTextureSize,
            0,
            0,
            x + arrowX,
            y + arrowY,
            progressArrowX(),
            progressArrowY()
        )
    }

    // Misc

    @Suppress("SameParameterValue")
    protected fun isMouseOver(
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

}