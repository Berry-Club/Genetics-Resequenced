package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneChangeEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity

class GeneAddedKubeEvent(
	val event: GeneChangeEvent
) : GeneKubeEvent {

	override fun getEntity(): LivingEntity = event.entity

	@Info("The Gene that's being added.")
	override fun getGene(): Holder<Gene> = event.geneHolder

	@Info("Whether or not the event is the Post event.")
	fun isPostEvent(): Boolean = event is GeneChangeEvent.Post

}
