package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneChangeEvent
import dev.latvian.mods.kubejs.entity.KubeLivingEntityEvent
import dev.latvian.mods.kubejs.typings.Info
import net.minecraft.world.entity.LivingEntity

class GeneChangeKubeEvent(
	val event: GeneChangeEvent
) : KubeLivingEntityEvent {

	override fun getEntity(): LivingEntity = event.entity

	@Info("""
		Whether or not the LivingEntity is gaining the Gene.
		
		If false, the Gene is being taken away.
	""")
	fun isAddition(): Boolean = event.isAddition

}