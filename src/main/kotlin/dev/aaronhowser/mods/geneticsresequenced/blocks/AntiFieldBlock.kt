package dev.aaronhowser.mods.geneticsresequenced.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Material
import thedarkcolour.kotlinforforge.forge.vectorutil.component1
import thedarkcolour.kotlinforforge.forge.vectorutil.component2
import thedarkcolour.kotlinforforge.forge.vectorutil.component3

object AntiFieldBlock : Block(Properties.of(Material.METAL)) {

    fun antifieldNear(level: Level, location: BlockPos): Boolean {
        val (x, y, z) = location

        val searchRange = -5..5
        for (dX in searchRange) for (dY in searchRange) for (dZ in searchRange) {
            if (level.getBlockState(BlockPos(x + dX, y + dY, z + dZ)).block == this) {
                return true
            }
        }

        return false
    }

}