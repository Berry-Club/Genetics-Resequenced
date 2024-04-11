package dev.aaronhowser.mods.geneticsresequenced.screens.renderer

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.renderer.Rect2i


abstract class InfoArea(
    protected val area: Rect2i
) : GuiComponent() {
    abstract fun draw(transform: PoseStack)
}