package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey

data class PlasmidProgressItemComponent(
    val geneRk: ResourceKey<Gene>,
    val dnaPoints: Int
) {

    companion object {

        val CODEC: Codec<PlasmidProgressItemComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceKey.codec(GeneRegistry.GENE_REGISTRY_KEY)
                    .fieldOf("gene")
                    .forGetter(PlasmidProgressItemComponent::geneRk),
                Codec.INT
                    .fieldOf("dnaPoints")
                    .forGetter(PlasmidProgressItemComponent::dnaPoints)
            ).apply(instance, ::PlasmidProgressItemComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, PlasmidProgressItemComponent> = StreamCodec.composite(
            ResourceKey.streamCodec(GeneRegistry.GENE_REGISTRY_KEY), PlasmidProgressItemComponent::geneRk,
            ByteBufCodecs.INT, PlasmidProgressItemComponent::dnaPoints,
            ::PlasmidProgressItemComponent
        )
    }

}