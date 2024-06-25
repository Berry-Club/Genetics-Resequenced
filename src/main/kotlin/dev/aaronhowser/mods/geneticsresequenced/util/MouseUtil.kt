package dev.aaronhowser.mods.geneticsresequenced.util

object MouseUtil {


    fun isMouseOver(
        mouseX: Int, mouseY: Int,
        x: Int, y: Int,
        sizeX: Int, sizeY: Int
    ): Boolean {
        val xRange = x..(x + sizeX)
        val yRange = y..(y + sizeY)

        return mouseX in xRange && mouseY in yRange
    }

}