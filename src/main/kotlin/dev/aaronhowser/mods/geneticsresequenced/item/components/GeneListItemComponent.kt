package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.HolderSetCodec

data class GeneListItemComponent(
    val geneHolderSet: HolderSet<Gene>
) {

    constructor(set: Set<Holder<Gene>>) : this(HolderSet.direct(*set.toTypedArray()))

    companion object {
        val CODEC: Codec<GeneListItemComponent> =
            HolderSetCodec.create(ModGenes.GENE_REGISTRY_KEY, Gene.CODEC, false)
                .xmap(::GeneListItemComponent, GeneListItemComponent::geneHolderSet)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GeneListItemComponent> = StreamCodec.composite(
            ByteBufCodecs.holderSet(ModGenes.GENE_REGISTRY_KEY), GeneListItemComponent::geneHolderSet,
            ::GeneListItemComponent
        )
    }

}