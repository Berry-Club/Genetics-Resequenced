package dev.aaronhowser.mods.geneticsresequenced.block.base.menu

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.renderer.Rect2i
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.energy.IEnergyStorage

class EnergyInfoArea(
    xMin: Int,
    yMin: Int,
    private val energy: IEnergyStorage? = null,
    width: Int = 8,
    height: Int = 64
) : Renderable {

    private val area = Rect2i(xMin, yMin, width, height)

    val tooltip: Component
        get() =
            Component
                .literal(
                    energy!!.energyStored.toString() + "/" + energy.maxEnergyStored + " FE"
                )

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        val energy = energy ?: return

        val totalHeight = area.height
        val percent = (totalHeight * (energy.energyStored / energy.maxEnergyStored.toFloat())).toInt()

        pGuiGraphics.fillGradient(
            area.x,
            area.y + (totalHeight - percent),
            area.x + area.width,
            area.y + totalHeight,
            -0x4aeb00,
            -0x9ff500
        )

    }
}