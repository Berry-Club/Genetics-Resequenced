package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronDataComponentRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.registries.DeferredHolder

object ModDataComponents : AaronDataComponentRegistry() {

	val GENE: DeferredHolder<DataComponentType<*>, DataComponentType<Holder<Gene>>> =
		register("gene", Gene.CODEC, Gene.STREAM_CODEC)

	val GENE_SET: DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
		register("genes", Gene.HOLDER_SET_CODEC, Gene.HOLDER_SET_STREAM_CODEC)

	val ANTIGENE_SET: DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
		register("antigenes", Gene.HOLDER_SET_CODEC, Gene.HOLDER_SET_STREAM_CODEC)

}