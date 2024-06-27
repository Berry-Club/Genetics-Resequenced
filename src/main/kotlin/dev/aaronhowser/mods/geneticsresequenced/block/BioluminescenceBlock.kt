package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AirBlock
import net.minecraft.world.level.block.state.BlockState

class BioluminescenceBlock :
    AirBlock(
        Properties
            .of()
            .replaceable()
            .noCollission()
            .instabreak()
            .noLootTable()
            .air()
            .lightLevel { LIGHT_LEVEL }
    ) {

    companion object {
        const val LIGHT_LEVEL = 10
    }

    override fun onPlace(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pOldState: BlockState,
        pMovedByPiston: Boolean
    ) {
        pLevel.scheduleTick(pPos, this, ServerConfig.bioluminescenceDuration.get())
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston)
    }

    override fun tick(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
        pLevel.removeBlock(pPos, false)
        super.tick(pState, pLevel, pPos, pRandom)
    }

}