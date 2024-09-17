package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey

data class GeneRkItemComponent(
    val geneRk: ResourceKey<Gene>
) {

    constructor(geneHolder: Holder<Gene>) : this(geneHolder.key!!)

    fun geneHolder(registries: HolderLookup.Provider): Holder<Gene>? {
        return geneRk.getHolder(registries)
    }

    fun getGeneHolderOrThrow(registries: HolderLookup.Provider): Holder<Gene> {
        return geneHolder(registries) ?: throw IllegalStateException("Gene $geneRk not found")
    }

    companion object {
        val CODEC: Codec<GeneRkItemComponent> =
            ResourceKey.codec(GeneRegistry.GENE_REGISTRY_KEY).xmap(::GeneRkItemComponent, GeneRkItemComponent::geneRk)

        val STREAM_CODEC: StreamCodec<ByteBuf, GeneRkItemComponent> =
            ResourceKey.streamCodec(GeneRegistry.GENE_REGISTRY_KEY)
                .map(::GeneRkItemComponent, GeneRkItemComponent::geneRk)
    }

}