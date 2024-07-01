package dev.aaronhowser.mods.geneticsresequenced.api.genes

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.RegistryBuilder

object GeneRegistry {

    val GENE_REGISTRY_KEY: ResourceKey<Registry<Gene>> =
        ResourceKey.createRegistryKey(OtherUtil.modResource("genes"))

    val GENE_REGISTRY: Registry<Gene> = RegistryBuilder(GENE_REGISTRY_KEY)
        .sync(true)
        .create()

}