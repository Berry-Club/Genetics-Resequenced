package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class PlasmidProgressItemComponent(
    val geneHolder: Holder<Gene>,
    val dnaPoints: Int
) {

    companion object {

        val CODEC: Codec<PlasmidProgressItemComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                Gene.CODEC.fieldOf("gene").forGetter(PlasmidProgressItemComponent::geneHolder),
                Codec.INT.fieldOf("dnaPoints").forGetter(PlasmidProgressItemComponent::dnaPoints)
            ).apply(instance, ::PlasmidProgressItemComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, PlasmidProgressItemComponent> = StreamCodec.composite(
            Gene.STREAM_CODEC, PlasmidProgressItemComponent::geneHolder,
            ByteBufCodecs.INT, PlasmidProgressItemComponent::dnaPoints,
            ::PlasmidProgressItemComponent
        )
    }

}