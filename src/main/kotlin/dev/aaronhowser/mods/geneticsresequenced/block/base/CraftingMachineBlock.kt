package dev.aaronhowser.mods.geneticsresequenced.block.base

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.BlockHitResult

abstract class CraftingMachineBlock(
    properties: Properties = Properties.of().sound(SoundType.METAL),
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

    override fun codec(): MapCodec<out HorizontalDirectionalBlock> {
        TODO("Not yet implemented")
    }

    override fun onRemove(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pNewState: BlockState,
        pIsMoving: Boolean
    ) {

        if (pState.block != pNewState.block) {
            val blockEntity = pLevel.getBlockEntity(pPos) as? CraftingMachineBlockEntity
                ?: throw IllegalStateException("No block entity found at $pPos")

            blockEntity.dropDrops()
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving)
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity? {
        return blockEntityType
            .getConstructor(BlockPos::class.java, BlockState::class.java)
            .newInstance(pPos, pState)
    }

    override fun useWithoutItem(
        pState: BlockState,
        pLevel: Level,
        pPos: BlockPos,
        pPlayer: Player,
        pHitResult: BlockHitResult
    ): InteractionResult {

        if (pPlayer !is ServerPlayer) {
            return InteractionResult.CONSUME
        }

        val blockEntity =
            pLevel.getBlockEntity(pPos) as? CraftingMachineBlockEntity
                ?: throw IllegalStateException("No block entity found at $pPos")

        pPlayer.openMenu(blockEntity, pPos)

        return InteractionResult.SUCCESS
    }

}