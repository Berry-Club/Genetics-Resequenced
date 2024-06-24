package dev.aaronhowser.mods.geneticsresequenced.block.base.menu

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.renderer.Rect2i

abstract class InfoArea(
    protected val area: Rect2i
) : AbstractWidget() {
    abstract fun draw(transform: PoseStack)
}