package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

data class GeneItemComponent(
    val geneHolder: Holder<Gene>
) {

    companion object {
        val CODEC: Codec<GeneItemComponent> =
            Gene.CODEC.xmap(::GeneItemComponent, GeneItemComponent::geneHolder)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GeneItemComponent> =
            Gene.STREAM_CODEC.map(::GeneItemComponent, GeneItemComponent::geneHolder)
    }

}