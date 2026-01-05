package dev.aaronhowser.mods.geneticsresequenced.event.custom

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.eventbus.api.Cancelable
import net.minecraftforge.eventbus.api.Event

sealed class GeneChangeEvent : Event() {

	abstract val entity: LivingEntity
	abstract val geneHolder: Holder<Gene>
	abstract val wasAdded: Boolean

	@Cancelable
	data class Pre(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>,
		override val wasAdded: Boolean
	) : GeneChangeEvent()

	data class Post(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>,
		override val wasAdded: Boolean
	) : GeneChangeEvent()

}