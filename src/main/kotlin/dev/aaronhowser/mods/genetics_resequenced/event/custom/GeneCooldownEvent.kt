package dev.aaronhowser.mods.genetics_resequenced.event.custom

import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.eventbus.api.Cancelable
import net.minecraftforge.eventbus.api.Event

sealed class GeneCooldownEvent : Event() {

	abstract val entity: LivingEntity
	abstract val geneHolder: Holder<Gene>

	@Cancelable
	data class Add(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>,
		var cooldownTicks: Int
	) : GeneCooldownEvent()

	data class Remove(
		override val entity: LivingEntity,
		override val geneHolder: Holder<Gene>
	) : GeneCooldownEvent()
}