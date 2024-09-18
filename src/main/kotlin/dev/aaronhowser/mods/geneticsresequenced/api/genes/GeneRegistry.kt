package dev.aaronhowser.mods.geneticsresequenced.api.genes

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isMutation
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

object GeneRegistry {

    val GENE_REGISTRY_KEY: ResourceKey<Registry<Gene>> =
        ResourceKey.createRegistryKey(OtherUtil.modResource("gene"))

    fun getGeneRegistry(registries: HolderLookup.Provider): HolderLookup.RegistryLookup<Gene> {
        return registries.lookupOrThrow(GENE_REGISTRY_KEY)
    }

    fun getAllGeneHolders(registries: HolderLookup.Provider): Stream<Holder.Reference<Gene>> {
        return getGeneRegistry(registries).listElements()
    }

    fun fromResourceKey(registries: HolderLookup.Provider, rk: ResourceKey<Gene>): Holder.Reference<Gene>? {
        return getGeneRegistry(registries).get(rk).getOrNull()
    }

    fun fromResourceLocation(registries: HolderLookup.Provider, rl: ResourceLocation): Holder.Reference<Gene>? {
        return fromResourceKey(registries, ResourceKey.create(GENE_REGISTRY_KEY, rl))
    }

    fun fromString(registries: HolderLookup.Provider, id: String): Holder<Gene>? {
        return fromResourceLocation(registries, ResourceLocation.parse(id))
    }

    fun fromIdPath(registries: HolderLookup.Provider, path: String): Holder.Reference<Gene>? {
        return getAllGeneHolders(registries).filter { it.value().id.path == path }.findFirst().orElse(null)
    }

    fun getRegistrySorted(registries: HolderLookup.Provider): List<Holder<Gene>> {
        val mutations = mutableListOf<Holder<Gene>>()
        val negatives = mutableListOf<Holder<Gene>>()
        val other = mutableListOf<Holder<Gene>>()

        for (geneHolder in getAllGeneHolders(registries)) {
            when {
                geneHolder.isMutation -> mutations.add(geneHolder)
                geneHolder.isNegative -> negatives.add(geneHolder)
                else -> other.add(geneHolder)
            }
        }

        return other.sortedBy { it.key } + mutations.sortedBy { it.key } + negatives.sortedBy { it.key }
    }

}