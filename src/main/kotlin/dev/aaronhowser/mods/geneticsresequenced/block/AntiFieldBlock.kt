package dev.aaronhowser.mods.geneticsresequenced.block

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
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

data class AntiFieldBlock(
    val properties: Properties = Properties
        .of()
        .sound(SoundType.METAL)
        .strength(0.3f)
) : Block(properties) {

    init {
        registerDefaultState(stateDefinition.any().setValue(DISABLED, false))
    }

    override fun codec(): MapCodec<out AntiFieldBlock> {
        return RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                propertiesCodec()
            ).apply(instance, ::AntiFieldBlock)
        }
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

        fun getNearestActiveAntifield(level: Level, location: BlockPos): Optional<BlockPos> {
            val radius = ServerConfig.antifieldBlockRadius.get()

            return BlockPos.findClosestMatch(location, radius, radius) { pos ->
                val blockState = level.getBlockState(pos)
                blockState.block == ModBlocks.ANTI_FIELD_BLOCK.get() && !blockState.getValue(DISABLED)
            }
        }

        fun isNearActiveAntifield(level: Level, location: BlockPos): Boolean {
            return getNearestActiveAntifield(level, location).isPresent
        }
    }


}