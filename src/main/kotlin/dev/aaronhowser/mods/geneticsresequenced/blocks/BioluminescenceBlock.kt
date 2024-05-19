package dev.aaronhowser.mods.geneticsresequenced.blocks

import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AirBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

@Suppress("OVERRIDE_DEPRECATION")
class BioluminescenceBlock :
    AirBlock(
        Properties
            .of(Material.AIR)
            .air()
            .instabreak()
            .noLootTable()
            .lightLevel { 10 }
    ) {

    override fun onPlace(pState: BlockState, pLevel: Level, pPos: BlockPos, pOldState: BlockState, pIsMoving: Boolean) {
        pLevel.scheduleTick(pPos, this, ServerConfig.bioluminescenceDuration.get())
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving)
    }

    override fun tick(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
        pLevel.removeBlock(pPos, false)
        super.tick(pState, pLevel, pPos, pRandom)
    }

}