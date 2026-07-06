package dev.aaronhowser.mods.genetics_resequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.genetics_resequenced.event.custom.GeneChangeEvent
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity

sealed class GeneRemovedKubeEvent : GeneKubeEvent {

	protected abstract val event: GeneChangeEvent

	override fun getEntity(): LivingEntity = event.entity

	@Info("The Gene that's being removed.")
	override fun getGene(): Holder<Gene> = event.geneHolder

	class Pre(
		override val event: GeneChangeEvent.Pre
	) : GeneRemovedKubeEvent()

	class Post(
		override val event: GeneChangeEvent.Post
	) : GeneRemovedKubeEvent()

}
