package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.geneticsresequenced.block_entity.AntiFieldBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.redstone.Orientation

class AntiFieldBlock(properties: BlockBehaviour.Properties) : Block(properties), EntityBlock {

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
		orientation: Orientation?,
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

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return AntiFieldBlockEntity(pos, state)
	}

	override fun <T : BlockEntity> getTicker(
		level: Level,
		state: BlockState,
		blockEntityType: BlockEntityType<T>
	): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			ModBlockEntityTypes.ANTI_FIELD_BLOCK.get(),
			AntiFieldBlockEntity::tick
		)
	}

	companion object {
		val DISABLED: BooleanProperty = BlockStateProperties.POWERED

		fun properties(): Properties {
			return Properties
				.of()
				.sound(SoundType.METAL)
				.strength(0.3f)
		}
	}


}
