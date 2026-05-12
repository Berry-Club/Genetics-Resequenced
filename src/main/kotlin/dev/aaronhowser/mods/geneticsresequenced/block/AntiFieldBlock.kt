package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty

class AntiFieldBlock : Block(
	Properties
		.of()
		.sound(SoundType.METAL)
		.strength(0.3f)
), EntityBlock {

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

	override fun canConnectRedstone(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction?): Boolean {
		return true
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
		TODO("Not yet implemented")
	}

	companion object {
		val DISABLED: BooleanProperty = BlockStateProperties.POWERED
	}


}