package dev.aaronhowser.mods.geneticsresequenced.block.base

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.Containers
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.phys.BlockHitResult

abstract class MachineBlock(
	properties: Properties = DEFAULT_PROPERTIES
) : Block(properties), EntityBlock {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(H_FACING, Direction.NORTH)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(H_FACING)
	}

	override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(H_FACING, ctx.horizontalDirection.opposite)
	}

	override fun use(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hand: InteractionHand,
		hitResult: BlockHitResult
	): InteractionResult {
		val be = level.getBlockEntity(pos)

		if (be is MenuProvider) {
			if (!level.isClientSide) {
				player.openMenu(be)
			}
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

	override fun <T : BlockEntity?> getTicker(
		level: Level,
		state: BlockState,
		type: BlockEntityType<T>
	): BlockEntityTicker<T>? {
		return BlockEntityTicker { l, p, s, be ->
			if (be is MachineBlockEntity) {
				MachineBlockEntity.tick(l, p, s, be)
			}
		}
	}

	override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, isMoving: Boolean) {
		if (!state.`is`(newState.block)) {
			val be = level.getBlockEntity(pos)
			if (be is MachineBlockEntity) {
				Containers.dropContents(level, pos, be.container)
			}
		}

		super.onRemove(state, level, pos, newState, isMoving)
	}

	companion object {
		val H_FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

		val DEFAULT_PROPERTIES: Properties = Properties.of()
			.mapColor(MapColor.METAL)
			.requiresCorrectToolForDrops()
			.strength(5f, 6f)
			.sound(SoundType.METAL)
	}
}