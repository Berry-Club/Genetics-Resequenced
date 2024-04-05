package dev.aaronhowser.mods.geneticsresequenced.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LightBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

@Suppress("OVERRIDE_DEPRECATION")
object BioluminescenceBlock :
    LightBlock(
        Properties
            .of(Material.AIR)
            .air()
            .instabreak()
            .noLootTable()
            .lightLevel { 15 }
    ) {
    override fun onPlace(pState: BlockState, pLevel: Level, pPos: BlockPos, pOldState: BlockState, pIsMoving: Boolean) {
        pLevel.scheduleTick(pPos, this, 20 * 10)
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving)
    }

    override fun tick(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
        pLevel.removeBlock(pPos, false)
        super.tick(pState, pLevel, pPos, pRandom)
    }

}