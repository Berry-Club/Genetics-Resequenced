package dev.aaronhowser.mods.geneticsresequenced.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import kotlin.jvm.optionals.getOrNull

class GeneRequirements(
	val gene: ResourceKey<Gene>,
	val requirements: List<ResourceKey<Gene>>
) {

	companion object {
		val REGISTRY_KEY: ResourceKey<Registry<GeneRequirements>> =
			ResourceKey.createRegistryKey(
				ResourceLocation.fromNamespaceAndPath(
					GeneticsResequenced.ID,
					"gene_requirements"
				)
			)

		val CODEC: Codec<GeneRequirements> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
						.fieldOf("gene")
						.forGetter(GeneRequirements::gene),
					ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
						.listOf()
						.fieldOf("requirements")
						.forGetter(GeneRequirements::requirements)
				).apply(instance, ::GeneRequirements)
			}

		fun getRequiredGeneHolders(
			gene: Holder<Gene>,
			registries: HolderLookup.Provider
		): Set<Holder<Gene>> {
			val registry = registries.lookupOrThrow(REGISTRY_KEY)

			val resultRks = mutableSetOf<ResourceKey<Gene>>()

			for (grHolder in registry.listElements()) {
				val gr = grHolder.value()
				if (gene.isGene(gr.gene)) {
					resultRks.addAll(gr.requirements)
				}
			}

			val geneRegistry = registries.lookupOrThrow(ModGenes.GENE_REGISTRY_KEY)
			return resultRks.mapNotNull { geneRegistry.get(it).getOrNull() }.toSet()
		}
	}

}