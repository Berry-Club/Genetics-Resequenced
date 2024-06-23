package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class IsActiveItemComponent(
    val value: Boolean
) {

    companion object {

        val CODEC: Codec<IsActiveItemComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BOOL.fieldOf("value").forGetter(IsActiveItemComponent::value)
            ).apply(instance, ::IsActiveItemComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, IsActiveItemComponent> = StreamCodec.composite(
            ByteBufCodecs.BOOL, IsActiveItemComponent::value,
            ::IsActiveItemComponent
        )

        val component: DataComponentType<IsActiveItemComponent> by lazy { ModDataComponents.IS_ACTIVE.get() }

    }

}