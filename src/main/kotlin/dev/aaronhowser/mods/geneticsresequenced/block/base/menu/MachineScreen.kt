package dev.aaronhowser.mods.geneticsresequenced.block.base.menu

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part.EnergyBar
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.part.ProgressArrow
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

    init {
        inventoryLabelY += 6
    }

    protected abstract val backgroundTexture: ResourceLocation
    protected open val backgroundSize: Int = ScreenTextures.Backgrounds.TEXTURE_SIZE

    override fun init() {
        super.init()

        progressArrow = ProgressArrow(
            x = leftPos + arrowLeftPos,
            y = topPos + arrowTopPos,
            arrowDirection = arrowDirection,
            font = font,
            percentDoneFunction = ::arrowPercentDone,
            shouldRenderProgress = ::shouldRenderProgressArrow,
            onClickFunction = ::clickedProgressArrow
        )

        energyBar = EnergyBar(
            x = leftPos + energyPosLeft,
            y = topPos + energyPosTop,
            energyStorage = menu.blockEntity.energyStorage,
            font = font
        )

        this.addRenderableWidget(progressArrow)
        this.addRenderableWidget(energyBar)
    }

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
    }

    // Energy Area

    protected lateinit var energyBar: EnergyBar
        private set

    protected open val energyPosLeft: Int = ScreenTextures.Elements.Energy.Location.Default.X
    protected open val energyPosTop: Int = ScreenTextures.Elements.Energy.Location.Default.Y

    // Progress arrow
    protected lateinit var progressArrow: ProgressArrow
        private set

    protected open val arrowDirection: ProgressArrow.ArrowDirection = ProgressArrow.ArrowDirection.RIGHT
    protected open val arrowLeftPos: Int = ScreenTextures.Elements.ArrowRight.Position.Default.X
    protected open val arrowTopPos: Int = ScreenTextures.Elements.ArrowRight.Position.Default.Y

    protected open fun arrowPercentDone(): Float = menu.getPercentDone()
    protected abstract fun shouldRenderProgressArrow(): Boolean
    protected open fun clickedProgressArrow(mouseX: Double, mouseY: Double, button: Int) {}

}