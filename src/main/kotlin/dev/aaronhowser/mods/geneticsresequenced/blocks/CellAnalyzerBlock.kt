package dev.aaronhowser.mods.geneticsresequenced.blocks

import dev.aaronhowser.mods.geneticsresequenced.blockentities.CellAnalyzerBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blockentities.ModBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.phys.BlockHitResult
import net.minecraftforge.network.NetworkHooks

object CellAnalyzerBlock : BaseEntityBlock(Properties.of(Material.METAL)) {


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
            if (blockEntity is CellAnalyzerBlockEntity) {
                blockEntity.drops()
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
        if (blockEntity is CellAnalyzerBlockEntity) {
            NetworkHooks.openScreen(pPlayer as ServerPlayer, blockEntity, pPos)
        } else {
            throw IllegalStateException("Missing block entity")
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit)
    }

    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity {
        return CellAnalyzerBlockEntity(pPos, pState)
    }

    override fun <T : BlockEntity?> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.CELL_ANALYZER.get(), CellAnalyzerBlockEntity::tick)
    }

}