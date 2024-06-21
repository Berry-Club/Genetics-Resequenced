package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import com.mojang.serialization.Codec


data class GeneContainer(
    val genes: Set<Gene>
) {
    constructor() : this(HashSet())

    companion object {

        val CODEC: Codec<GeneContainer> = Gene.CODEC.listOf().xmap(
            { list: List<Gene> ->
                GeneContainer(
                    HashSet<Gene>(list)
                )
            },
            { genes: GeneContainer ->
                ArrayList<Gene>(
                    genes.genes
                )
            })

    }
}