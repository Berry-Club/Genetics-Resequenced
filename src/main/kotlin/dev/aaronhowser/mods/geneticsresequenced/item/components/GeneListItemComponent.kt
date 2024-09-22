package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey

data class GeneListItemComponent(
    val geneRks: Set<ResourceKey<Gene>>
) {
    constructor(collection: Collection<ResourceKey<Gene>>) : this(collection.toSet())

    companion object {

        val CODEC: Codec<GeneListItemComponent> = ResourceKey.codec(GeneRegistry.GENE_REGISTRY_KEY).listOf().xmap(
            { GeneListItemComponent(it) },
            { it.geneRks.toList() })

        val STREAM_CODEC: StreamCodec<ByteBuf, GeneListItemComponent> =
            ResourceKey.streamCodec(GeneRegistry.GENE_REGISTRY_KEY).apply(ByteBufCodecs.list()).map(
                { GeneListItemComponent(it) },
                { it.geneRks.toList() }
            )
    }

}