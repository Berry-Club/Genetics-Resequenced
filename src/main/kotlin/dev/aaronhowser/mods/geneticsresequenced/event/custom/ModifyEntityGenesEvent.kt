package dev.aaronhowser.mods.geneticsresequenced.event.custom

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.neoforged.bus.api.Event

data class ModifyEntityGenesEvent(
	val entityType: ResourceKey<EntityType<*>>,
	val geneWeights: MutableMap<ResourceKey<Gene>, Int>
) : Event() {

	fun getTotalWeight(): Int = geneWeights.values.sum()
	fun getEntityTypeString(): String = entityType.identifier().toString()

	fun getWeight(gene: ResourceKey<Gene>): Int = geneWeights.getOrDefault(gene, 0)

	fun setWeight(gene: ResourceKey<Gene>, weight: Int) {
		if (weight <= 0) {
			geneWeights.remove(gene)
			return
		}

		geneWeights[gene] = weight
	}

	fun remove(gene: ResourceKey<Gene>) = setWeight(gene, 0)

}