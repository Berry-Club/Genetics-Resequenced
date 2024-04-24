package dev.aaronhowser.mods.geneticsresequenced.blocks

import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.Material
import thedarkcolour.kotlinforforge.forge.vectorutil.component1
import thedarkcolour.kotlinforforge.forge.vectorutil.component2
import thedarkcolour.kotlinforforge.forge.vectorutil.component3

// Apparently blocks with special states need to be classes, not objects
class AntiFieldBlock : Block(Properties.of(Material.METAL)) {

    init {
        registerDefaultState(stateDefinition.any().setValue(DISABLED, false))
    }

    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState? {
        return defaultBlockState().setValue(DISABLED, false)
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(pBuilder)
        pBuilder.add(DISABLED)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun neighborChanged(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pBlock: Block,
        pFromPos: BlockPos,
        pIsMoving: Boolean
    ) {
        val isPowered = pLevel.hasNeighborSignal(pPos)

        if (pState.getValue(DISABLED) != isPowered) {
            pLevel.setBlock(pPos, pState.setValue(DISABLED, isPowered), 2)
        }

    }

    companion object {
        val DISABLED: BooleanProperty = BlockStateProperties.POWERED

        /**
         * TODO:
         * @see BlockPos.findClosestMatch
         */
        fun antifieldNear(level: Level, location: BlockPos): Boolean {
            val (x, y, z) = location

            val searchRange = -5..5
            for (dX in searchRange) for (dY in searchRange) for (dZ in searchRange) {
                val blockState = level.getBlockState(BlockPos(x + dX, y + dY, z + dZ))
                if (blockState.block == ModBlocks.ANTIFIELD_BLOCK && !blockState.getValue(DISABLED)) {
                    return true
                }
            }

            return false
        }
    }

}