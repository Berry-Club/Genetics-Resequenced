package dev.aaronhowser.mods.genetics_resequenced.block

import dev.aaronhowser.mods.genetics_resequenced.block.base.MachineBlock
import dev.aaronhowser.mods.genetics_resequenced.block_entity.CoalGeneratorBlockEntity
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty

class CoalGeneratorBlock(properties: Properties) : MachineBlock(::CoalGeneratorBlockEntity, properties) {

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

	companion object {
		val BURNING: BooleanProperty = BlockStateProperties.LIT
	}

}
