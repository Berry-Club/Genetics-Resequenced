package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.registry.AaronDataComponentRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.components.PlasmidProgressItemComponent
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.registries.DeferredHolder

object ModDataComponents : AaronDataComponentRegistry() {

	val IS_ACTIVE: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		boolean("is_active")

	val IS_CONTAMINATED: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		boolean("is_contaminated")

	val IS_INFINITY_ARROW: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		boolean("is_infinity_arrow")

	val DRAGON_HEALTH_CRYSTAL_DAMAGE: DeferredHolder<DataComponentType<*>, DataComponentType<Float>> =
		float("dragon_health_crystal_damage")

	val GENE: DeferredHolder<DataComponentType<*>, DataComponentType<Holder<Gene>>> =
		register("gene", Gene.CODEC, Gene.STREAM_CODEC)

	val GENE_SET: DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
		register("genes", Gene.HOLDER_SET_CODEC, Gene.HOLDER_SET_STREAM_CODEC)

	val ANTIGENE_SET: DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
		register("antigenes", Gene.HOLDER_SET_CODEC, Gene.HOLDER_SET_STREAM_CODEC)

}