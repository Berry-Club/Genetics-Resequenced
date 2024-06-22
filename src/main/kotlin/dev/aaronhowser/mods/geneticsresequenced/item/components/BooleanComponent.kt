package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class BooleanComponent(
    val value: Boolean
) {
    companion object {

        val CODEC: Codec<BooleanComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BOOL.fieldOf("value").forGetter(BooleanComponent::value)
            ).apply(instance, ::BooleanComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BooleanComponent> = StreamCodec.composite(
            ByteBufCodecs.BOOL, BooleanComponent::value,
            ::BooleanComponent
        )

        val component: DataComponentType<BooleanComponent> by lazy { ModDataComponents.BOOLEAN_COMPONENT.get() }

    }

}