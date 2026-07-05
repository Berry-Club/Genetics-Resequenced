package dev.aaronhowser.mods.genetics_resequenced.event.custom

import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent

sealed class GeneChangeEvent : Event() {

	abstract val entity: LivingEntity
	abstract val geneHolder: Holder<Gene>
	abstract val isAddition: Boolean

	data class Pre(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>,
		override val isAddition: Boolean
	) : GeneChangeEvent(), ICancellableEvent

	data class Post(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>,
		override val isAddition: Boolean
	) : GeneChangeEvent()

}