package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class BooleanItemComponent(
    val value: Boolean
) {

    companion object {
        val CODEC: Codec<BooleanItemComponent> =
            Codec.BOOL.xmap(::BooleanItemComponent, BooleanItemComponent::value)

        val STREAM_CODEC: StreamCodec<ByteBuf, BooleanItemComponent> =
            ByteBufCodecs.BOOL.map(::BooleanItemComponent, BooleanItemComponent::value)

    }

}