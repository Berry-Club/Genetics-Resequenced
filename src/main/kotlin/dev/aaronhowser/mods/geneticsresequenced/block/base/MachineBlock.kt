package dev.aaronhowser.mods.geneticsresequenced.block.base

import dev.aaronhowser.mods.aaron.block.SimpleContainerBlock
import dev.aaronhowser.mods.geneticsresequenced.block_entity.base.MachineBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
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

abstract class MachineBlock : SimpleContainerBlock(
	Properties.of()
		.mapColor(MapColor.METAL)
		.requiresCorrectToolForDrops()
		.strength(5f, 6f)
		.sound(SoundType.METAL)
), EntityBlock {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(H_FACING, Direction.NORTH)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(H_FACING)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
		return defaultBlockState()
			.setValue(H_FACING, context.horizontalDirection.opposite)
	}

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		val blockEntity = level.getBlockEntity(pos)

		if (blockEntity is MenuProvider) {
			player.openMenu(blockEntity)
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

	override fun <T : BlockEntity> getTicker(
		level: Level,
		state: BlockState,
		blockEntityType: BlockEntityType<T>
	): BlockEntityTicker<T> {
		return BlockEntityTicker { l, p, s, be ->
			if (be is MachineBlockEntity) {
				MachineBlockEntity.tick(l, p, s, be)
			}
		}
	}

	companion object {
		val H_FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
	}

}