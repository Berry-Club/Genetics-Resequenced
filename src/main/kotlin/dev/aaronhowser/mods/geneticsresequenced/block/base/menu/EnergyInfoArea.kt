package dev.aaronhowser.mods.geneticsresequenced.block.base.menu

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.Rect2i
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.energy.IEnergyStorage

//class EnergyInfoArea(
//    xMin: Int,
//    yMin: Int,
//    private val energy: IEnergyStorage? = null,
//    width: Int = 8,
//    height: Int = 64
//) :
//    InfoArea(Rect2i(xMin, yMin, width, height)) {
//
//    val tooltip: Component
//        get() =
//            Component
//                .literal(
//                    energy!!.energyStored.toString() + "/" + energy.maxEnergyStored + " FE"
//                )
//
//
//    override fun draw(transform: PoseStack) {
//        val height = area.height
//        val stored = (height * (energy!!.energyStored / energy.maxEnergyStored.toFloat())).toInt()
//        fillGradient(
//            transform,
//            area.x,
//            area.y + (height - stored),
//            area.x + area.width,
//            area.y + area.height,
//            -0x4aeb00, -0x9ff500
//        )
//    }
//}