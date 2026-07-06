package dev.aaronhowser.mods.genetics_resequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.genetics_resequenced.event.custom.TemporaryGeneRemovedEvent
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity

sealed class TemporaryGeneRemovedKubeEvent : GeneKubeEvent {

	protected abstract val event: TemporaryGeneRemovedEvent

	override fun getEntity(): LivingEntity = event.entity

	@Info("The temporary Gene that's being removed.")
	override fun getGene(): Holder<Gene> = event.geneHolder

	class Pre(
		override val event: TemporaryGeneRemovedEvent.Pre
	) : TemporaryGeneRemovedKubeEvent()

	class Post(
		override val event: TemporaryGeneRemovedEvent.Post
	) : TemporaryGeneRemovedKubeEvent()

}
