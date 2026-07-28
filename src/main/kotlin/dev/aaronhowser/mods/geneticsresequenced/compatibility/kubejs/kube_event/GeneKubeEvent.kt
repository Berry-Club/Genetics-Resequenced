package dev.aaronhowser.mods.geneticsresequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.latvian.mods.kubejs.entity.KubeLivingEntityEvent
import net.minecraft.core.Holder

interface GeneKubeEvent : KubeLivingEntityEvent {
	fun getGene(): Holder<Gene>
}