package dev.aaronhowser.mods.genetics_resequenced.block

import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import java.util.*
import java.util.WeakHashMap

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

	override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
		return defaultBlockState().setValue(DISABLED, false)
	}

	override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
		super.createBlockStateDefinition(pBuilder)
		pBuilder.add(DISABLED)
	}

	override fun neighborChanged(
		pState: BlockState,
		pLevel: Level,
		pPos: BlockPos,
		pNeighborBlock: Block,
		pNeighborPos: BlockPos,
		pMovedByPiston: Boolean
	) {
		val isPowered = pLevel.hasNeighborSignal(pPos)
		val wasPowered = pState.getValue(DISABLED)

		if (isPowered != wasPowered) {
			pLevel.setBlock(pPos, pState.setValue(DISABLED, isPowered), 3)
			updateTrackedState(pLevel, pPos, !isPowered)
		}
	}

	override fun onPlace(
		state: BlockState,
		level: Level,
		position: BlockPos,
		oldState: BlockState,
		movedByPiston: Boolean
	) {
		super.onPlace(state, level, position, oldState, movedByPiston)
		updateTrackedState(level, position, !state.getValue(DISABLED))
	}

	override fun onRemove(
		state: BlockState,
		level: Level,
		position: BlockPos,
		newState: BlockState,
		movedByPiston: Boolean
	) {
		if (!state.`is`(newState.block)) {
			updateTrackedState(level, position, false)
		}

		super.onRemove(state, level, position, newState, movedByPiston)
	}

	companion object {
		val DISABLED: BooleanProperty = BlockStateProperties.POWERED
		private val activeAntiFieldsByLevel: MutableMap<Level, MutableSet<BlockPos>> = WeakHashMap()

		private fun updateTrackedState(level: Level, position: BlockPos, isActive: Boolean) {
			val positions = activeAntiFieldsByLevel.getOrPut(level) { mutableSetOf() }
			if (isActive) {
				positions.add(position.immutable())
			} else {
				positions.remove(position)
			}
		}

		fun getNearestActiveAntifield(level: Level, location: BlockPos): Optional<BlockPos> {
			val radius = ServerConfig.CONFIG.antifieldBlockRadius.get()
			val positions = activeAntiFieldsByLevel[level] ?: return Optional.empty()
			var nearestPosition: BlockPos? = null
			var nearestDistance = Double.MAX_VALUE
			val invalidPositions = mutableListOf<BlockPos>()

			for (position in positions) {
				val state = level.getBlockState(position)
				if (state.block != ModBlocks.ANTI_FIELD_BLOCK.get() || state.getValue(DISABLED)) {
					invalidPositions.add(position)
					continue
				}

				val distance = position.distSqr(location)
				if (distance <= radius * radius && distance < nearestDistance) {
					nearestPosition = position
					nearestDistance = distance
				}
			}

			positions.removeAll(invalidPositions.toSet())
			return Optional.ofNullable(nearestPosition)
		}

		fun isNearActiveAntifield(level: Level, location: BlockPos): Boolean {
			return getNearestActiveAntifield(level, location).isPresent
		}
	}


}