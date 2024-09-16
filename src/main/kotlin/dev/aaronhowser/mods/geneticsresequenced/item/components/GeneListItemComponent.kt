package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class GeneListItemComponent(
    val genes: Set<Gene>
) {

    constructor() : this(HashSet())

    companion object {

        val CODEC: Codec<GeneListItemComponent> = Gene.CODEC.listOf().xmap(
            { list: List<Gene> ->
                GeneListItemComponent(
                    HashSet<Gene>(list)
                )
            },
            { genes: GeneListItemComponent ->
                ArrayList<Gene>(
                    genes.genes
                )
            })

        val STREAM_CODEC: StreamCodec<ByteBuf, GeneListItemComponent> = ByteBufCodecs.fromCodec(CODEC)
    }

}