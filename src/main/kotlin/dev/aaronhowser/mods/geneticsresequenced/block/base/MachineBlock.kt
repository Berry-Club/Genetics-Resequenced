package dev.aaronhowser.mods.geneticsresequenced.block.base

import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.material.MapColor

abstract class MachineBlock(
    val properties: Properties = defaultProperties
) : HorizontalDirectionalBlock(properties), EntityBlock {

    companion object {
        val defaultProperties: Properties = Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5f, 6f)
            .sound(SoundType.METAL)
    }

}