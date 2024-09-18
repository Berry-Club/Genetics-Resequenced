package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey

data class GeneRkItemComponent(
    val geneRk: ResourceKey<Gene>
) {

    companion object {
        val CODEC: Codec<GeneRkItemComponent> =
            ResourceKey.codec(GeneRegistry.GENE_REGISTRY_KEY).xmap(::GeneRkItemComponent, GeneRkItemComponent::geneRk)

        val STREAM_CODEC: StreamCodec<ByteBuf, GeneRkItemComponent> =
            ResourceKey.streamCodec(GeneRegistry.GENE_REGISTRY_KEY)
                .map(::GeneRkItemComponent, GeneRkItemComponent::geneRk)
    }

}