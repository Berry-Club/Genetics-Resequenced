package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneAddedKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneCooldownKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneRemovedKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.ModifyEntityGenesKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.ModifyGeneRequirementsKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneChangeEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneCooldownEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.ModifyEntityGenesEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.ModifyGeneRequirementsEvent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = GeneticsResequenced.MOD_ID)
object GeneticsJsEventHandler {

	@SubscribeEvent
	fun beforeGeneChange(event: GeneChangeEvent.Pre) {
		if (event.isAddition) {
			if (GeneticsJS.GENE_ADDED.hasListeners()) {
				GeneticsJS.GENE_ADDED.post(GeneAddedKubeEvent(event)).applyCancel(event)
			}
		} else {
			if (GeneticsJS.GENE_REMOVED.hasListeners()) {
				GeneticsJS.GENE_REMOVED.post(GeneRemovedKubeEvent(event)).applyCancel(event)
			}
		}
	}

	@SubscribeEvent
	fun afterGeneChange(event: GeneChangeEvent.Post) {
		if (event.isAddition) {
			if (GeneticsJS.GENE_ADDED.hasListeners()) {
				GeneticsJS.GENE_ADDED.post(GeneAddedKubeEvent(event))
			}
		} else {
			if (GeneticsJS.GENE_REMOVED.hasListeners()) {
				GeneticsJS.GENE_REMOVED.post(GeneRemovedKubeEvent(event))
			}
		}
	}

	@SubscribeEvent
	fun addGeneCooldown(event: GeneCooldownEvent.Add) {
		if (GeneticsJS.GENE_COOLDOWN_ADDED.hasListeners()) {
			GeneticsJS.GENE_COOLDOWN_ADDED.post(GeneCooldownKubeEvent.Add(event)).applyCancel(event)
		}
	}

	@SubscribeEvent
	fun removeGeneCooldown(event: GeneCooldownEvent.Add) {
		if (GeneticsJS.GENE_COOLDOWN_REMOVED.hasListeners()) {
			GeneticsJS.GENE_COOLDOWN_REMOVED.post(GeneCooldownKubeEvent.Add(event))
		}
	}

	@SubscribeEvent
	fun modifyGeneWeights(event: ModifyEntityGenesEvent) {
		if (GeneticsJS.MODIFY_GENE_WEIGHTS.hasListeners()) {
			GeneticsJS.MODIFY_GENE_WEIGHTS.post(ModifyEntityGenesKubeEvent(event))
		}
	}

	@SubscribeEvent
	fun modifyGeneRequirements(event: ModifyGeneRequirementsEvent) {
		if (GeneticsJS.MODIFY_GENE_REQUIREMENTS.hasListeners()) {
			GeneticsJS.MODIFY_GENE_REQUIREMENTS.post(ModifyGeneRequirementsKubeEvent(event))
		}
	}

}
