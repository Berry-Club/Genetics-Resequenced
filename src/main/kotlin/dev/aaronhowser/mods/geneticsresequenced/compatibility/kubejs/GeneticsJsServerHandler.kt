package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event.GeneChangeKubeEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneChangeEvent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(modid = GeneticsResequenced.MOD_ID)
object GeneticsJsServerHandler {

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

}