package dev.aaronhowser.mods.genetics_resequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.genetics_resequenced.event.custom.GeneCooldownEvent
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity

sealed class GeneCooldownKubeEvent : GeneKubeEvent {

	protected abstract val event: GeneCooldownEvent

	override fun getEntity(): LivingEntity = event.entity

	@Info("The Gene that's on cooldown")
	override fun getGene(): Holder<Gene> = event.geneHolder

	class Add(
		override val event: GeneCooldownEvent.Add
	) : GeneCooldownKubeEvent() {
		@Info("How many ticks the cooldown will be")
		fun getDuration(): Int = event.cooldownTicks

		@Info("Sets how many ticks the cooldown will be")
		fun setDuration(durationTicks: Int) {
			event.cooldownTicks = durationTicks
		}
	}

	class Remove(
		override val event: GeneCooldownEvent.Remove
	) : GeneCooldownKubeEvent()

}
