package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class GenesItemComponent(
    val genes: Set<Gene>
) {

    constructor() : this(HashSet())

    companion object {

        val CODEC: Codec<GenesItemComponent> = Gene.CODEC.listOf().xmap(
            { list: List<Gene> ->
                GenesItemComponent(
                    HashSet<Gene>(list)
                )
            },
            { genes: GenesItemComponent ->
                ArrayList<Gene>(
                    genes.genes
                )
            })

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Gene> = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Gene.CODEC), GenesItemComponent::genes,
            ::GenesItemComponent
        )

    }

}