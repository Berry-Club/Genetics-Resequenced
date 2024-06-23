package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
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

        val STREAM_CODEC: StreamCodec<ByteBuf, GenesItemComponent> = ByteBufCodecs.fromCodec(CODEC)

        val genesComponent: DataComponentType<GenesItemComponent> by lazy { ModDataComponents.GENES_COMPONENT.get() }
        val antigenesComponent: DataComponentType<GenesItemComponent> = ModDataComponents.ANTIGENES_COMPONENT.get()

    }

}