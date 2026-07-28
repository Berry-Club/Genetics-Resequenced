package dev.aaronhowser.mods.geneticsresequenced.event.custom

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent

sealed class TemporaryGeneRemovedEvent : Event() {

	abstract val entity: LivingEntity
	abstract val geneHolder: Holder<Gene>

	data class Pre(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>
	) : TemporaryGeneRemovedEvent(), ICancellableEvent

	data class Post(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>
	) : TemporaryGeneRemovedEvent()

}
