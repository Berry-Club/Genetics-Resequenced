package dev.aaronhowser.mods.genetics_resequenced.block

import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AirBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class BioluminescenceBlock :
	AirBlock(
		Properties
			.ofFullCopy(Blocks.AIR)
			.lightLevel { LIGHT_LEVEL }
	) {

	override fun onPlace(
		pState: BlockState,
		level: Level,
		pPos: BlockPos,
		pOldState: BlockState,
		pMovedByPiston: Boolean
	) {
		level.scheduleTick(pPos, this, ServerConfig.CONFIG.bioluminescenceDuration.get())
		super.onPlace(pState, level, pPos, pOldState, pMovedByPiston)
	}

	override fun tick(pState: BlockState, level: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
		if (level.getBlockState(pPos).`is`(this)) {
			level.removeBlock(pPos, false)
		}
		super.tick(pState, level, pPos, pRandom)
	}

	companion object {
		const val LIGHT_LEVEL = 10
	}

}