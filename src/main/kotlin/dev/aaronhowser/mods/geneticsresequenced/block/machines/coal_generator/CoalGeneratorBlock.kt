package dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty

class CoalGeneratorBlock : HorizontalDirectionalBlock(Properties.of().sound(SoundType.METAL)), EntityBlock {

    companion object {
        val BURNING: BooleanProperty = BlockStateProperties.LIT
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
        registerDefaultState(stateDefinition.any().setValue(BURNING, false))
    }


    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
        return defaultBlockState()
            .setValue(FACING, pContext.horizontalDirection.opposite)
            .setValue(BURNING, false)
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(pBuilder)
        pBuilder.add(FACING)
        pBuilder.add(BURNING)
    }

    // BLOCK ENTITY STUFF

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun onRemove(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNewState: BlockState,
        pMovedByPiston: Boolean
    ) {

        if (pState.block != pNewState.block) {
            val blockEntity = pLevel.getBlockEntity(pPos)
            if (blockEntity is CoalGeneratorBlockEntity) {
                blockEntity.dropDrops()
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston)
    }

    override fun codec(): MapCodec<out HorizontalDirectionalBlock> {
        TODO("Not yet implemented")
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity? {
        TODO("Not yet implemented")
    }
}