package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class PlasmidProgressItemComponent(
    val gene: Gene,
    val dnaPoints: Int
) {

    companion object {

        val CODEC: Codec<PlasmidProgressItemComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                Gene.CODEC.fieldOf("gene").forGetter(PlasmidProgressItemComponent::gene),
                Codec.INT.fieldOf("dnaPoints").forGetter(PlasmidProgressItemComponent::dnaPoints)
            ).apply(instance, ::PlasmidProgressItemComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, PlasmidProgressItemComponent> = StreamCodec.composite(
            Gene.STREAM_CODEC, PlasmidProgressItemComponent::gene,
            ByteBufCodecs.INT, PlasmidProgressItemComponent::dnaPoints,
            ::PlasmidProgressItemComponent
        )
    }

}