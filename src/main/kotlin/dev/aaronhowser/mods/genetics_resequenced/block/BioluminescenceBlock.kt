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
		blockState: BlockState,
		level: Level,
		pos: BlockPos,
		oldState: BlockState,
		movedByPiston: Boolean
	) {
		level.scheduleTick(pos, this, ServerConfig.CONFIG.bioluminescenceDuration.get())
	}

	override fun tick(
		blockState: BlockState,
		level: ServerLevel,
		pos: BlockPos,
		random: RandomSource
	) {
		if (level.getBlockState(pos).`is`(this)) {
			level.removeBlock(pos, false)
		}
	}

	companion object {
		const val LIGHT_LEVEL = 10
	}

}