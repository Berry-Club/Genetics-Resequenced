package dev.aaronhowser.mods.geneticsresequenced.util

import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType

object BlockEntityHelper {

    /**
     * @see BaseEntityBlock.createTickerHelper
     */
    fun <E : BlockEntity, A : BlockEntity> createTickerHelper(
        pServerType: BlockEntityType<A>,
        pClientType: BlockEntityType<E>,
        pTicker: BlockEntityTicker<in E>
    ): BlockEntityTicker<A>? {
        return if (pClientType === pServerType) pTicker as BlockEntityTicker<A> else null
    }

}