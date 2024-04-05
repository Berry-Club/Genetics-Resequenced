package dev.aaronhowser.mods.geneticsresequenced.block

import net.minecraft.world.level.block.AirBlock
import net.minecraft.world.level.material.Material

object BioluminescenceBlock :
    AirBlock(
        Properties
            .of(Material.AIR)
            .air()
            .instabreak()
            .noLootTable()
            .lightLevel { 15 }
    ) {
}