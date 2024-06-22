package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneBuilder
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil

//TODO: Do this eventually
object GeneRegistry {

//    val GENE_REGISTRY_KEY: ResourceKey<Registry<Gene>> =
//        ResourceKey.createRegistryKey(OtherUtil.modResource("genes"))
//
//    val GENE_REGISTRY: Registry<Gene> = RegistryBuilder(GENE_REGISTRY_KEY)
//        .sync(true)
//        .create()

    fun register() {}

    val testGene = GeneBuilder(OtherUtil.modResource("test_gene"))
        .build()
    val another = GeneBuilder(OtherUtil.modResource("another"))
        .build()

}