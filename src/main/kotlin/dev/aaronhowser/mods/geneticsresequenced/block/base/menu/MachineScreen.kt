package dev.aaronhowser.mods.geneticsresequenced.block.base.menu

import dev.aaronhowser.mods.geneticsresequenced.util.MouseUtil
import net.minecraft.client.gui.Font
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

    protected val font: Font by lazy { super.font }

    init {
        inventoryLabelY += 6
    }

    protected abstract val backgroundTexture: ResourceLocation
    protected open val backgroundSize: Int = ScreenTextures.Backgrounds.TEXTURE_SIZE

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
        renderEnergy(pGuiGraphics, leftPos, topPos)
    }

    override fun renderTooltip(guiGraphics: GuiGraphics, x: Int, y: Int) {
        renderEnergyAreaTooltip(guiGraphics, leftPos, topPos, x, y)

        super.renderTooltip(guiGraphics, x, y)
    }

    // Energy Area

    protected open val energyPosLeft: Int = ScreenTextures.Elements.Energy.Location.Default.X
    protected open val energyPosTop: Int = ScreenTextures.Elements.Energy.Location.Default.Y
    protected open fun renderEnergy(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        val energyStorage = menu.blockEntity.energyStorage
        val percentFull = energyStorage.energyStored.toFloat() / energyStorage.maxEnergyStored.toFloat()

        val energyTotalHeight = ScreenTextures.Elements.Energy.Dimensions.HEIGHT
        val energyCurrentHeight = (energyTotalHeight.toDouble() * percentFull).toInt()

        pGuiGraphics.blitSprite(
            ScreenTextures.Elements.Energy.TEXTURE,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            0,
            energyTotalHeight - energyCurrentHeight,
            x + energyPosLeft,
            y + energyPosTop + energyTotalHeight - energyCurrentHeight,
            ScreenTextures.Elements.Energy.TEXTURE_SIZE,
            energyCurrentHeight
        )
    }

    protected open fun renderEnergyAreaTooltip(pGuiGraphics: GuiGraphics, x: Int, y: Int, pMouseX: Int, pMouseY: Int) {
        if (!isMouseOver(
                pMouseX, pMouseY,
                x, y,
                energyPosLeft,
                energyPosTop,
                ScreenTextures.Elements.Energy.Dimensions.WIDTH,
                ScreenTextures.Elements.Energy.Dimensions.HEIGHT
            )
        ) return

        val energyStorage = menu.blockEntity.energyStorage

        pGuiGraphics.renderComponentTooltip(
            font,
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
    protected open val arrowPosLeft: Int = ScreenTextures.Elements.ArrowRight.Position.Default.X
    protected open val arrowPosTop: Int = ScreenTextures.Elements.ArrowRight.Position.Default.Y
    protected abstract fun shouldRenderProgressArrow(): Boolean
    protected abstract fun progressArrowWidth(): Int
    protected abstract fun progressArrowHeight(): Int

    protected open fun renderProgressArrow(pGuiGraphics: GuiGraphics, x: Int, y: Int) {
        if (!shouldRenderProgressArrow()) return

        pGuiGraphics.blitSprite(
            arrowTexture,
            arrowTextureSize,
            arrowTextureSize,
            0,
            0,
            x + arrowPosLeft,
            y + arrowPosTop,
            progressArrowWidth(),
            progressArrowHeight()
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