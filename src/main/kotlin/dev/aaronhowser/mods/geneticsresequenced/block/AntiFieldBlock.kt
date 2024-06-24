package dev.aaronhowser.mods.geneticsresequenced.block

import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
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

class AntiFieldBlock : Block(
    Properties
        .of()
        .sound(SoundType.METAL)
        .strength(0.3f)
) {

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

    override fun neighborChanged(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNeighborBlock: Block,
        pNeighborPos: BlockPos,
        pMovedByPiston: Boolean
    ) {

        val isPowered = pLevel.hasNeighborSignal(pPos)

        if (pState.getValue(DISABLED) != isPowered) {
            pLevel.setBlock(pPos, pState.setValue(DISABLED, isPowered), 2)
        }

    }

    companion object {
        val DISABLED: BooleanProperty = BlockStateProperties.POWERED

        fun getNearestAntifield(level: Level, location: BlockPos): Optional<BlockPos> {
            val radius = ServerConfig.antifieldBlockRadius.get()
            return BlockPos.findClosestMatch(location, radius, radius) { pos ->
                val blockState = level.getBlockState(pos)
                blockState.block == ModBlocks.ANTI_FIELD_BLOCK && !blockState.getValue(DISABLED)
            }
        }

        fun locationIsNearAntifield(level: Level, location: BlockPos): Boolean {
            return getNearestAntifield(level, location).isPresent
        }
    }


}