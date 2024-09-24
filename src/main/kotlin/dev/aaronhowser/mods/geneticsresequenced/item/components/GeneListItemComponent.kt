package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class GeneListItemComponent(
    val geneHolders: Set<Holder<Gene>>
) {

    companion object {

        val CODEC: Codec<GeneListItemComponent> = Gene.CODEC.listOf().xmap(
            { GeneListItemComponent(it.toSet()) },
            { it.geneHolders.toList() })

        val STREAM_CODEC: StreamCodec<ByteBuf, GeneListItemComponent> = ByteBufCodecs.fromCodec(CODEC)
    }

}