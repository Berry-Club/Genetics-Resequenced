package dev.aaronhowser.mods.geneticsresequenced.api.genes

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

    fun getRegistrySorted(registries: HolderLookup.Provider): List<Gene> {
        val mutations = mutableListOf<Gene>()
        val negatives = mutableListOf<Gene>()
        val other = mutableListOf<Gene>()

        for (gene in getAllGeneHolders(registries).map { it.value() }) {
            when {
                gene.isMutation(registries) -> mutations.add(gene)
                gene.isNegative -> negatives.add(gene)
                else -> other.add(gene)
            }
        }

        return other.sortedBy { it.id } + mutations.sortedBy { it.id } + negatives.sortedBy { it.id }
    }

}