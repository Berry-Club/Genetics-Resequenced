package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.geneticsresequenced.event.custom.ModifyGeneRequirementsEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.latvian.mods.kubejs.event.KubeEvent
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey

class ModifyGeneRequirementsKubeEvent(
	private val event: ModifyGeneRequirementsEvent
) : KubeEvent {

	@Info("The Gene whose requirements are being modified")
	fun getGene(): Holder<Gene> = event.gene

	@Info("A mutable set of the required Genes")
	fun getRequirements(): MutableSet<ResourceKey<Gene>> = event.requirements

	@Info("Make it require a Gene")
	fun add(geneRk: ResourceKey<Gene>): Boolean = getRequirements().add(geneRk)

	@Info("Stop making it require a Gene")
	fun remove(geneRk: ResourceKey<Gene>): Boolean = getRequirements().remove(geneRk)

	@Info("Check if it requires a Gene")
	fun contains(geneRk: ResourceKey<Gene>): Boolean = getRequirements().contains(geneRk)

}