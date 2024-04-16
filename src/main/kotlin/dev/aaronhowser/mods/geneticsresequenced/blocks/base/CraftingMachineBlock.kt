package dev.aaronhowser.mods.geneticsresequenced.blocks.base

import dev.aaronhowser.mods.geneticsresequenced.block_entities.base.CraftingMachineBlockEntity
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
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.BlockHitResult
import net.minecraftforge.network.NetworkHooks

open class CraftingMachineBlock(
    properties: Properties = Properties.of(Material.METAL),
    private val blockEntityType: Class<out CraftingMachineBlockEntity>
) : HorizontalDirectionalBlock(properties), EntityBlock {

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

            // god i fucking hate this this is so gross
            try {
                val asType = blockEntityType.cast(blockEntity)
                asType.drops()
            } catch (e: ClassCastException) {
                /* Continue */
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity? {
        return blockEntityType
            .getConstructor(BlockPos::class.java, BlockState::class.java)
            .newInstance(pPos, pState)
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

        try {
            val blockEntity = pLevel.getBlockEntity(pPos)
            val asType = blockEntityType.cast(blockEntity)
            NetworkHooks.openScreen(
                pPlayer as ServerPlayer,
                asType,
                pPos
            )
        } catch (e: ClassCastException) {
            throw IllegalStateException("Missing block entity")
        }

        return InteractionResult.SUCCESS
    }

}