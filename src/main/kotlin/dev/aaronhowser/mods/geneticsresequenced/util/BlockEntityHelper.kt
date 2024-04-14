package dev.aaronhowser.mods.geneticsresequenced.util

import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType

object BlockEntityHelper {

    fun <E : BlockEntity, A : BlockEntity> createTickerHelper(
        pServerType: BlockEntityType<A>,
        pClientType: BlockEntityType<E>,
        pTicker: BlockEntityTicker<in E>
    ): BlockEntityTicker<A>? {
        return if (pClientType === pServerType) pTicker as BlockEntityTicker<A> else null
    }

}