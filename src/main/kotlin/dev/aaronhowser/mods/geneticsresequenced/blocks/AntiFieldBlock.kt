package dev.aaronhowser.mods.geneticsresequenced.blocks

import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registries.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.Material
import java.util.*

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

        fun getNearestAntifield(level: Level, location: BlockPos): Optional<BlockPos> {
            val radius = ServerConfig.antifieldBlockRadius.get()
            return BlockPos.findClosestMatch(location, radius, radius) { pos ->
                val blockState = level.getBlockState(pos)
                blockState.block == ModBlocks.ANTIFIELD_BLOCK && !blockState.getValue(DISABLED)
            }
        }

        fun locationIsNearAntifield(level: Level, location: BlockPos): Boolean {
            return getNearestAntifield(level, location).isPresent
        }
    }

}