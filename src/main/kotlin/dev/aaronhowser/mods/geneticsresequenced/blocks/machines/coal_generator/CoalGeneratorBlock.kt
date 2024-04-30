package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.util.BlockEntityHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.BlockHitResult
import net.minecraftforge.network.NetworkHooks

class CoalGeneratorBlock : HorizontalDirectionalBlock(Properties.of(Material.METAL)), EntityBlock {

    companion object {
        val BURNING: BooleanProperty = BlockStateProperties.LIT
    }

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
        registerDefaultState(stateDefinition.any().setValue(BURNING, false))
    }

    override fun getStateForPlacement(pContext: BlockPlaceContext): BlockState {
        return defaultBlockState()
            .setValue(FACING, pContext.horizontalDirection.opposite)
            .setValue(BURNING, false)
    }

    override fun createBlockStateDefinition(pBuilder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(pBuilder)
        pBuilder.add(FACING)
        pBuilder.add(BURNING)
    }

    // BLOCK ENTITY STUFF BELOW

    @Suppress("OVERRIDE_DEPRECATION")
    override fun getRenderShape(pState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onRemove(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNewState: BlockState,
        pIsMoving: Boolean
    ) {

        if (pState.block != pNewState.block) {
            val blockEntity = pLevel.getBlockEntity(pPos)
            if (blockEntity is CoalGeneratorBlockEntity) {
                blockEntity.dropDrops()
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun use(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHand: InteractionHand,
        pHit: BlockHitResult
    ): InteractionResult {

        if (pLevel.isClientSide) {
            return InteractionResult.sidedSuccess(pLevel.isClientSide)
        }

        val blockEntity = pLevel.getBlockEntity(pPos)
        if (blockEntity is CoalGeneratorBlockEntity) {
            NetworkHooks.openScreen(pPlayer as ServerPlayer, blockEntity, pPos)
        } else {
            throw IllegalStateException("Missing block entity")
        }

        return InteractionResult.SUCCESS
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return CoalGeneratorBlockEntity(pPos, pState)
    }

    override fun <T : BlockEntity> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return BlockEntityHelper.createTickerHelper(
            pBlockEntityType,
            ModBlockEntities.COAL_GENERATOR.get(),
            CoalGeneratorBlockEntity::tick
        )
    }
}