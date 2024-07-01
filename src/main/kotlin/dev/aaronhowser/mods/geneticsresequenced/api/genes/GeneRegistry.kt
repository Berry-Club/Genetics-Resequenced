package dev.aaronhowser.mods.geneticsresequenced.api.genes

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.RegistryBuilder

object GeneRegistry {

    val GENE_REGISTRY_KEY: ResourceKey<Registry<Gene>> =
        ResourceKey.createRegistryKey(OtherUtil.modResource("genes"))

    val GENE_REGISTRY: Registry<Gene> = RegistryBuilder(GENE_REGISTRY_KEY)
        .sync(true)
        .create()

    fun fromId(id: ResourceLocation): Gene? = GENE_REGISTRY.get(id)
    fun fromId(id: String): Gene? = GENE_REGISTRY.get(ResourceLocation.parse(id))

}