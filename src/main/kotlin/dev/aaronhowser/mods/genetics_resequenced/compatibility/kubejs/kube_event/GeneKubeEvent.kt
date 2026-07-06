package dev.aaronhowser.mods.genetics_resequenced.compatibility.kubejs.kube_event

import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.latvian.mods.kubejs.entity.KubeLivingEntityEvent
import net.minecraft.core.Holder

interface GeneKubeEvent : KubeLivingEntityEvent {
	fun getGene(): Holder<Gene>
}
