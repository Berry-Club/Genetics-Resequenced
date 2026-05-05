package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty

class AntiFieldBlock : Block(
	Properties
		.of()
		.sound(SoundType.METAL)
		.strength(0.3f)
) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(DISABLED, false)
		)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
		return defaultBlockState()
			.setValue(
				DISABLED,
				context.level.hasNeighborSignal(context.clickedPos)
			)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(DISABLED)
	}

	override fun neighborChanged(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		neighborBlock: Block,
		neighborPos: BlockPos,
		movedByPiston: Boolean
	) {
		val isPowered = level.hasNeighborSignal(pos)
		val wasPowered = state.getValue(DISABLED)

		if (isPowered != wasPowered) {
			level.setBlockAndUpdate(pos, state.setValue(DISABLED, isPowered))
		}
	}

	companion object {
		val DISABLED: BooleanProperty = BlockStateProperties.POWERED

		fun isNearActiveAntifield(entity: Entity): Boolean {
			return isNearActiveAntifield(entity.level(), entity.blockPosition())
		}

		fun isNearActiveAntifield(level: Level, location: BlockPos): Boolean {
			val radius = ServerConfig.CONFIG.antifieldBlockRadius.get()

			val nearbyBlocks = BlockPos.betweenClosed(
				location.offset(-radius, -radius, -radius),
				location.offset(radius, radius, radius)
			)

			for (pos in nearbyBlocks) {
				val state = level.getBlockState(pos)
				if (!state.isBlock(ModBlocks.ANTI_FIELD_BLOCK.get())) continue
				if (!state.getValue(DISABLED)) return true
			}

			return false
		}
	}


}