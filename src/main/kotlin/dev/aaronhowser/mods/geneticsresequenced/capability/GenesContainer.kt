package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder

class GenesContainer : IGenesContainer {
	override var genes: MutableSet<Holder<Gene>> = mutableSetOf()

	override fun add(gene: Holder<Gene>): Boolean = genes.add(gene)
	override fun remove(gene: Holder<Gene>): Boolean = genes.remove(gene)
	override fun has(gene: Holder<Gene>): Boolean = genes.contains(gene)
}