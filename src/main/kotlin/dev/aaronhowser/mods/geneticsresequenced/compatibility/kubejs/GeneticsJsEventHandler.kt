package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneChangeKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneCooldownKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.ModifyEntityGenesKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneChangeEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneCooldownEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.ModifyEntityGenesEvent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = GeneticsResequenced.MOD_ID)
object GeneticsJsEventHandler {

	@SubscribeEvent
	fun beforeGeneChange(event: GeneChangeEvent.Pre) {
		if (GeneticsJS.GENE_CHANGED.hasListeners()) {
			GeneticsJS.GENE_CHANGED.post(GeneChangeKubeEvent(event)).applyCancel(event)
		}
	}

	@SubscribeEvent
	fun afterGeneChange(event: GeneChangeEvent.Post) {
		if (GeneticsJS.GENE_CHANGED.hasListeners()) {
			GeneticsJS.GENE_CHANGED.post(GeneChangeKubeEvent(event))
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

}