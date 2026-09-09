package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs

import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.*
import dev.aaronhowser.mods.geneticsresequenced.event.custom.*
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.SubscribeEvent

object GeneticsJsEventHandler {

	@SubscribeEvent
	fun beforeGeneChange(event: GeneChangeEvent.Pre) {
		val handler = if (event.isAddition) {
			GeneticsJS.GENE_ADDED_PRE
		} else {
			GeneticsJS.GENE_REMOVED_PRE
		}

		if (handler.hasListeners()) {
			val kubeEvent = if (event.isAddition) {
				GeneAddedKubeEvent.Pre(event)
			} else {
				GeneRemovedKubeEvent.Pre(event)
			}

			handler.post(kubeEvent, getGeneKey(event.geneHolder)).applyCancel(event)
		}
	}

	@SubscribeEvent
	fun afterGeneChange(event: GeneChangeEvent.Post) {
		val handler = if (event.isAddition) {
			GeneticsJS.GENE_ADDED_POST
		} else {
			GeneticsJS.GENE_REMOVED_POST
		}

		if (handler.hasListeners()) {
			val kubeEvent = if (event.isAddition) {
				GeneAddedKubeEvent.Post(event)
			} else {
				GeneRemovedKubeEvent.Post(event)
			}

			handler.post(kubeEvent, getGeneKey(event.geneHolder))
		}
	}

	@SubscribeEvent
	fun addGeneCooldown(event: GeneCooldownEvent.Add) {
		if (GeneticsJS.GENE_COOLDOWN_ADDED.hasListeners()) {
			GeneticsJS.GENE_COOLDOWN_ADDED.post(
				GeneCooldownKubeEvent.Add(event),
				getGeneKey(event.geneHolder)
			).applyCancel(event)
		}
	}

	@SubscribeEvent
	fun beforeTemporaryGeneAdded(event: TemporaryGeneAddedEvent.Pre) {
		if (GeneticsJS.TEMPORARY_GENE_ADDED_PRE.hasListeners()) {
			GeneticsJS.TEMPORARY_GENE_ADDED_PRE.post(
				TemporaryGeneAddedKubeEvent.Pre(event),
				getGeneKey(event.geneHolder)
			).applyCancel(event)
		}
	}

	@SubscribeEvent
	fun afterTemporaryGeneAdded(event: TemporaryGeneAddedEvent.Post) {
		if (GeneticsJS.TEMPORARY_GENE_ADDED_POST.hasListeners()) {
			GeneticsJS.TEMPORARY_GENE_ADDED_POST.post(
				TemporaryGeneAddedKubeEvent.Post(event),
				getGeneKey(event.geneHolder)
			)
		}
	}

	@SubscribeEvent
	fun beforeTemporaryGeneRemoved(event: TemporaryGeneRemovedEvent.Pre) {
		if (GeneticsJS.TEMPORARY_GENE_REMOVED_PRE.hasListeners()) {
			GeneticsJS.TEMPORARY_GENE_REMOVED_PRE.post(
				TemporaryGeneRemovedKubeEvent.Pre(event),
				getGeneKey(event.geneHolder)
			).applyCancel(event)
		}
	}

	@SubscribeEvent
	fun afterTemporaryGeneRemoved(event: TemporaryGeneRemovedEvent.Post) {
		if (GeneticsJS.TEMPORARY_GENE_REMOVED_POST.hasListeners()) {
			GeneticsJS.TEMPORARY_GENE_REMOVED_POST.post(
				TemporaryGeneRemovedKubeEvent.Post(event),
				getGeneKey(event.geneHolder)
			)
		}
	}

	@SubscribeEvent
	fun removeGeneCooldown(event: GeneCooldownEvent.Remove) {
		if (GeneticsJS.GENE_COOLDOWN_REMOVED.hasListeners()) {
			GeneticsJS.GENE_COOLDOWN_REMOVED.post(
				GeneCooldownKubeEvent.Remove(event),
				getGeneKey(event.geneHolder)
			)
		}
	}

	@SubscribeEvent
	fun modifyGeneWeights(event: ModifyEntityGenesEvent) {
		if (GeneticsJS.MODIFY_GENE_WEIGHTS.hasListeners()) {
			GeneticsJS.MODIFY_GENE_WEIGHTS.post(
				ModifyEntityGenesKubeEvent(event),
				event.entityType
			)
		}
	}

	@SubscribeEvent
	fun modifyGeneRequirements(event: ModifyGeneRequirementsEvent) {
		if (GeneticsJS.MODIFY_GENE_REQUIREMENTS.hasListeners()) {
			GeneticsJS.MODIFY_GENE_REQUIREMENTS.post(
				ModifyGeneRequirementsKubeEvent(event),
				getGeneKey(event.gene)
			)
		}
	}

	private fun getGeneKey(geneHolder: Holder<Gene>): ResourceKey<Gene> {
		return geneHolder.unwrapKey().orElseThrow()
	}

}