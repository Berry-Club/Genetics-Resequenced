package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneChangeEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity

sealed class GeneAddedKubeEvent : GeneKubeEvent {

	protected abstract val event: GeneChangeEvent

	override fun getEntity(): LivingEntity = event.entity

	@Info("The Gene that's being added.")
	override fun getGene(): Holder<Gene> = event.geneHolder

	class Pre(
		override val event: GeneChangeEvent.Pre
	) : GeneAddedKubeEvent()

	class Post(
		override val event: GeneChangeEvent.Post
	) : GeneAddedKubeEvent()

}
