package dev.aaronhowser.mods.geneticsresequenced.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

object GeneRequirements {

	val REGISTRY_KEY: ResourceKey<Registry<GeneRequirementsData>> =
		ResourceKey.createRegistryKey(
			ResourceLocation.fromNamespaceAndPath(
				GeneticsResequenced.ID,
				"gene_requirements"
			)
		)

	data class GeneRequirementsData(
		val requirements: List<ResourceKey<Gene>>
	) {
		companion object {
			val CODEC: Codec<GeneRequirementsData> =
				RecordCodecBuilder.create { instance ->
					instance.group(
						ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
							.listOf()
							.fieldOf("requirements")
							.forGetter(GeneRequirementsData::requirements)
					).apply(instance, ::GeneRequirementsData)
				}
		}
	}

	fun getRequiredGeneHolders(
		gene: Holder<Gene>,
		registries: HolderLookup.Provider
	): Set<Holder<Gene>> {
		val registry = registries.lookupOrThrow(REGISTRY_KEY)
		val geneRk = gene.key ?: return emptySet()
		val geneRequirementsRk = ResourceKey.create(REGISTRY_KEY, geneRk.location())

		val requirementsData = registry
			.get(geneRequirementsRk)
			.orElse(null)
			?: return emptySet()

		return requirementsData
			.value()
			.requirements
			.map { it.getHolderOrThrow(registries) }
			.toSet()
	}
}