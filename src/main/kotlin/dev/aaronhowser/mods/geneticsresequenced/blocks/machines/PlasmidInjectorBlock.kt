package dev.aaronhowser.mods.geneticsresequenced.blocks.machines

import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.Material

object PlasmidInjectorBlock : HorizontalDirectionalBlock(Properties.of(Material.METAL)) {

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(FACING, pContext.horizontalDirection.opposite)
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(pBuilder)
        pBuilder.add(FACING)
    }

}