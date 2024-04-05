package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.geneticsresequenced.blockentity.BioluminescenceBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blockentity.ModBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.LightBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

object BioluminescenceBlock :
    LightBlock(
        Properties
            .of(Material.AIR)
            .air()
            .instabreak()
            .noLootTable()
            .lightLevel { 15 }
    ) {
}