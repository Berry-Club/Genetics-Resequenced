package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.geneticsresequenced.event.custom.ModifyEntityGenesEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.latvian.mods.kubejs.event.KubeEvent
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType

class ModifyEntityGenesKubeEvent(
	private val event: ModifyEntityGenesEvent
) : KubeEvent {

	@Info("Get the entity type whose gene weights are being modified")
	fun getEntityType(): ResourceKey<EntityType<*>> = event.entityType

	@Info("Get the map of ResourceKey<Gene> to Int weight")
	fun getWeights(): MutableMap<ResourceKey<Gene>, Int> = event.geneWeights

	@Info("Get the integer weight of the Gene")
	fun getWeight(geneRk: ResourceKey<Gene>): Int = event.getWeight(geneRk)

	@Info("Get the total weight of all genes for the entity")
	fun getTotalWeight(): Int = event.getTotalWeight()

	@Info("Set the weight of the Gene")
	fun setWeight(geneRk: ResourceKey<Gene>, weight: Int) = event.setWeight(geneRk, weight)

	@Info("Remove the Gene from the weights")
	fun remove(geneRk: ResourceKey<Gene>) = event.remove(geneRk)

}