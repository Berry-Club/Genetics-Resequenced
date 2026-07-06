package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneChangeEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.latvian.mods.kubejs.entity.KubeLivingEntityEvent
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity

class GeneChangeKubeEvent(
	val event: GeneChangeEvent
) : KubeLivingEntityEvent {

	override fun getEntity(): LivingEntity = event.entity

	@Info(
		"The Gene that's being added or removed."
	)
	fun getGene(): Holder<Gene> = event.geneHolder

	@Info(
		"""
		Whether or not the LivingEntity is gaining the Gene.
		
		If false, the Gene is being taken away.
	"""
	)
	fun isAddition(): Boolean = event.isAddition

	@Info("Whether or not the event is the Post event.")
	fun isPostEvent(): Boolean = event is GeneChangeEvent.Post

}