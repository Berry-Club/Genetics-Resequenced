package dev.aaronhowser.mods.geneticsresequenced.util

object MouseUtil {
    fun isMouseOver(mouseX: Double, mouseY: Double, x: Int, y: Int): Boolean {
        return isMouseOver(mouseX, mouseY, x, y, 16)
    }

    fun isMouseOver(mouseX: Double, mouseY: Double, x: Int, y: Int, size: Int): Boolean {
        return isMouseOver(mouseX, mouseY, x, y, size, size)
    }

    fun isMouseOver(mouseX: Double, mouseY: Double, x: Int, y: Int, sizeX: Int, sizeY: Int): Boolean {
        return mouseX >= x && mouseX <= x + sizeX && mouseY >= y && mouseY <= y + sizeY
    }
}