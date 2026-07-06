package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneCooldownEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity

sealed class GeneAddCooldownKubeEvent : GeneKubeEvent {

	protected abstract val event: GeneCooldownEvent

	override fun getEntity(): LivingEntity = event.entity

	@Info("The Gene that's on cooldown")
	override fun getGene(): Holder<Gene> = event.geneHolder

	class Add(
		override val event: GeneCooldownEvent.Add
	) : GeneAddCooldownKubeEvent() {
		@Info("How many ticks the cooldown will be")
		fun getDuration(): Int = event.cooldownTicks
	}

	class Remove(
		override val event: GeneCooldownEvent.Remove
	) : GeneAddCooldownKubeEvent()

}