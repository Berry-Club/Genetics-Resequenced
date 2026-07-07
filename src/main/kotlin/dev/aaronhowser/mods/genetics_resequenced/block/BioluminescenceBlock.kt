package dev.aaronhowser.mods.genetics_resequenced.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AirBlock
import net.minecraft.world.level.block.state.BlockState

class BioluminescenceBlock(properties: Properties) : AirBlock(properties) {

	override fun onPlace(
		pState: BlockState,
		pLevel: Level,
		pPos: BlockPos,
		pOldState: BlockState,
		pMovedByPiston: Boolean
	) {
		pLevel.scheduleTick(pPos, this, ServerConfig.CONFIG.bioluminescenceDuration.get())
		super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston)
	}

	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		if (level.getBlockState(pos).isBlock(this)) {
			level.removeBlock(pos, false)
		}
	}

	companion object {
		const val LIGHT_LEVEL = 10

		fun defaultProperties(): Properties {
			return Properties
				.of()
				.replaceable()
				.noCollision()
				.instabreak()
				.noLootTable()
				.air()
				.lightLevel { LIGHT_LEVEL }
		}
	}

}
