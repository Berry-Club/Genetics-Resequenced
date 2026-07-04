package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.AirBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState

class BioluminescenceBlock(properties: BlockBehaviour.Properties) : AirBlock(properties) {

	companion object {
		const val LIGHT_LEVEL = 10

		fun properties(): BlockBehaviour.Properties {
			return BlockBehaviour.Properties
				.of()
				.replaceable()
				.noCollision()
				.instabreak()
				.noLootTable()
				.air()
				.lightLevel { LIGHT_LEVEL }
		}
	}

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

	override fun tick(pState: BlockState, pLevel: ServerLevel, pPos: BlockPos, pRandom: RandomSource) {
		if (pLevel.getBlockState(pPos).`is`(this)) {
			pLevel.removeBlock(pPos, false)
		}
		super.tick(pState, pLevel, pPos, pRandom)
	}

}
