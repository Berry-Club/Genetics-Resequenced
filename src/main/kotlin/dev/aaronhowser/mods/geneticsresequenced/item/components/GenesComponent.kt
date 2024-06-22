package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneContainer

class GenesComponent(
    val genes: Set<Gene>
) {

    constructor() : this(HashSet())

    companion object {

        val CODEC: Codec<GenesComponent> = GeneContainer.CODEC.xmap(
            { geneContainer: GeneContainer ->
                GenesComponent(
                    geneContainer.genes
                )
            },
            { genesComponent: GenesComponent ->
                GeneContainer(
                    genesComponent.genes
                )
            }
        )



    }

}