package dev.aaronhowser.mods.geneticsresequenced.api.genes

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderLookup.Provider
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import java.util.stream.Stream

object GeneRegistry {

    val GENE_REGISTRY_KEY: ResourceKey<Registry<Gene>> =
        ResourceKey.createRegistryKey(OtherUtil.modResource("gene"))

    fun getGeneRegistry(registries: HolderLookup.Provider): HolderLookup.RegistryLookup<Gene> {
        return registries.lookupOrThrow(GENE_REGISTRY_KEY)
    }

    fun getAllGeneHolders(registries: Provider): Stream<Holder.Reference<Gene>> {
        return getGeneRegistry(registries).listElements()
    }

//    val GENE_REGISTRY: Registry<Gene> = RegistryBuilder(GENE_REGISTRY_KEY)
//        .sync(true)
//        .create()

//    fun fromResourceLocation(id: ResourceLocation): Gene? = GENE_REGISTRY.get(id)
//    fun fromString(id: String): Gene? = GENE_REGISTRY.get(ResourceLocation.parse(id))

//    fun fromIdPath(path: String): Gene? = GENE_REGISTRY.find { it.id.path == path }

//    fun getRegistrySorted(): List<Gene> {
//        val mutations = mutableListOf<Gene>()
//        val negatives = mutableListOf<Gene>()
//        val other = mutableListOf<Gene>()
//
//        for (gene in GENE_REGISTRY) {
//            when {
//                gene.isMutation -> mutations.add(gene)
//                gene.isNegative -> negatives.add(gene)
//                else -> other.add(gene)
//            }
//        }
//
//        return other.sortedBy { it.id } + mutations.sortedBy { it.id } + negatives.sortedBy { it.id }
//    }

}