package dev.aaronhowser.mods.geneticsresequenced.event.custom

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent

sealed class GeneCooldownEvent : Event() {

	abstract val entity: LivingEntity
	abstract val geneHolder: Holder<Gene>

	//TODO: Make cooldown ticks modifiable
	data class Add(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>,
		val cooldownTicks: Int
	) : GeneCooldownEvent(), ICancellableEvent

	data class Remove(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>
	) : GeneCooldownEvent()

}