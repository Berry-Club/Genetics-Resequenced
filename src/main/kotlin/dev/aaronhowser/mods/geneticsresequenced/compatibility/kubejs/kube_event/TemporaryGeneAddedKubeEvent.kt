package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.geneticsresequenced.event.custom.TemporaryGeneAddedEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity

sealed class TemporaryGeneAddedKubeEvent : GeneKubeEvent {

	protected abstract val event: TemporaryGeneAddedEvent

	override fun getEntity(): LivingEntity = event.entity

	@Info("The temporary Gene that's being added.")
	override fun getGene(): Holder<Gene> = event.geneHolder

	@Info("How many ticks the temporary Gene will last.")
	fun getDuration(): Int = event.durationTicks

	class Pre(
		override val event: TemporaryGeneAddedEvent.Pre
	) : TemporaryGeneAddedKubeEvent()

	class Post(
		override val event: TemporaryGeneAddedEvent.Post
	) : TemporaryGeneAddedKubeEvent()

}
