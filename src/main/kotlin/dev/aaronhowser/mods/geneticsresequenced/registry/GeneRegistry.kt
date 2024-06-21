package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneBuilder
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

//TODO: Do this eventually
object GeneRegistry {

    val GENE_REGISTRY_KEY: ResourceKey<Registry<Gene>> =
        ResourceKey.createRegistryKey(OtherUtil.modResource("genes"))

    val GENE_REGISTRY: Registry<Gene> = RegistryBuilder(GENE_REGISTRY_KEY)
        .sync(true)
        .create()


    val testGene = GeneBuilder(OtherUtil.modResource("test_gene"))
        .build()
    val another = GeneBuilder(OtherUtil.modResource("another"))
        .build()

}