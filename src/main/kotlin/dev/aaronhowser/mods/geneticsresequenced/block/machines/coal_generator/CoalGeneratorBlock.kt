package dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.BlockHitResult

data class CoalGeneratorBlock(
    val properties: Properties,
    val facing: Direction = Direction.NORTH,
    val burning: Boolean = false
) : HorizontalDirectionalBlock(properties), EntityBlock {

    companion object {
        val BURNING: BooleanProperty = BlockStateProperties.LIT
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
        registerDefaultState(stateDefinition.any().setValue(BURNING, false))
    }

    override fun codec(): MapCodec<out CoalGeneratorBlock> {
        return RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                propertiesCodec(),
                BlockStateProperties.HORIZONTAL_FACING.codec().fieldOf("facing").forGetter { it.facing },
                Codec.BOOL.fieldOf("isBurning").forGetter { it.burning }
            ).apply(instance, ::CoalGeneratorBlock)
        }
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

    override fun useWithoutItem(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHitResult: BlockHitResult
    ): InteractionResult {

        if (pLevel.isClientSide) {
            return InteractionResult.PASS
        }

        val blockEntity = pLevel.getBlockEntity(pPos) as? CoalGeneratorBlockEntity
            ?: throw IllegalStateException("No block entity found at $pPos")

        pPlayer.openMenu(blockEntity)

        return InteractionResult.SUCCESS
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return CoalGeneratorBlockEntity(pPos, pState)
    }
}