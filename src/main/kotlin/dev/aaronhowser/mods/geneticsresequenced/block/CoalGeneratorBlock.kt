package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlock
import dev.aaronhowser.mods.geneticsresequenced.block.block_entity.CoalGeneratorBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty

class CoalGeneratorBlock : MachineBlock() {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(H_FACING, Direction.NORTH)
				.setValue(BURNING, false)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		super.createBlockStateDefinition(builder)
		builder.add(BURNING)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
		return super.getStateForPlacement(context)
			.setValue(BURNING, false)
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return CoalGeneratorBlockEntity(pos, state)
	}

	companion object {
		val BURNING: BooleanProperty = BlockStateProperties.LIT
	}

}