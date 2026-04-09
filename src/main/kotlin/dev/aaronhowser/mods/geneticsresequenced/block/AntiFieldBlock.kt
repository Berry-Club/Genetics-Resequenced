package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.redstone.Orientation
import java.util.*

class AntiFieldBlock : Block(
	Properties
		.of()
		.sound(SoundType.METAL)
		.strength(0.3f)
) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(POWERED, false)
		)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
		return defaultBlockState()
			.setValue(POWERED, context.level.hasNeighborSignal(context.clickedPos))
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(POWERED)
	}

	override fun neighborChanged(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		block: Block,
		orientation: Orientation?,
		movedByPiston: Boolean
	) {
		val isPowered = level.hasNeighborSignal(pos)
		val wasPowered = state.getValue(POWERED)

		if (wasPowered != isPowered) {
			level.setBlock(pos, state.setValue(POWERED, isPowered), UPDATE_CLIENTS)
		}
	}

	companion object {
		val POWERED: BooleanProperty = BlockStateProperties.POWERED

		fun getNearestActiveAntifield(level: Level, location: BlockPos): Optional<BlockPos> {
			val radius = ServerConfig.CONFIG.antifieldBlockRadius.get()

			return BlockPos.findClosestMatch(location, radius, radius) { pos ->
				val blockState = level.getBlockState(pos)
				blockState.block == ModBlocks.ANTI_FIELD_BLOCK.get() && !blockState.getValue(POWERED)
			}
		}

		fun isNearActiveAntifield(level: Level, location: BlockPos): Boolean {
			return getNearestActiveAntifield(level, location).isPresent
		}
	}


}