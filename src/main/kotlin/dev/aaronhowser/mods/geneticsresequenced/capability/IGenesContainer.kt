package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraftforge.common.capabilities.AutoRegisterCapability

@AutoRegisterCapability
interface IGenesContainer {
	var genes: MutableSet<Holder<Gene>>

	fun add(gene: Holder<Gene>): Boolean
	fun remove(gene: Holder<Gene>): Boolean
	fun has(gene: Holder<Gene>): Boolean

}