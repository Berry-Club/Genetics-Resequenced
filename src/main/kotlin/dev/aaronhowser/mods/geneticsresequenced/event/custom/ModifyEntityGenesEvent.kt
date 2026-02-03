package dev.aaronhowser.mods.geneticsresequenced.event.custom

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.neoforged.bus.api.Event

data class ModifyEntityGenesEvent(
	val entityType: ResourceKey<EntityType<*>>,
	val geneWeights: MutableMap<ResourceKey<Gene>, Int>
) : Event() {

	fun getTotalWeight(): Int = geneWeights.values.sum()

	fun getWeight(gene: ResourceKey<Gene>): Int = geneWeights.getOrDefault(gene, 0)

	// For KubeJS
	fun getWeight(geneString: String): Int {
		val geneRl = ResourceLocation.parse(geneString)
		val geneRk = ResourceKey.create(ModGenes.GENE_REGISTRY_KEY, geneRl)
		return getWeight(geneRk)
	}

	fun setWeight(gene: ResourceKey<Gene>, weight: Int) {
		geneWeights[gene] = weight
	}

	// For KubeJS
	fun setWeight(geneString: String, weight: Int) {
		val geneRl = ResourceLocation.parse(geneString)
		val geneRk = ResourceKey.create(ModGenes.GENE_REGISTRY_KEY, geneRl)
		setWeight(geneRk, weight)
	}

}