package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.EntitySnapshot
import dev.aaronhowser.mods.aaron.registry.AaronDataComponentRegistry
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.components.PlasmidProgressItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents : AaronDataComponentRegistry() {

	val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
		DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, GeneticsResequenced.ID)

	override fun getDataComponentRegistry(): DeferredRegister.DataComponents = DATA_COMPONENT_REGISTRY

	val SPECIFIC_ENTITY: DeferredHolder<DataComponentType<*>, DataComponentType<SpecificEntityItemComponent>> =
		register(
			"specific_entity",
			SpecificEntityItemComponent.CODEC,
			SpecificEntityItemComponent.STREAM_CODEC
		)

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

	val PLASMID_PROGRESS: DeferredHolder<DataComponentType<*>, DataComponentType<PlasmidProgressItemComponent>> =
		register(
			"plasmid_progress",
			PlasmidProgressItemComponent.CODEC,
			PlasmidProgressItemComponent.STREAM_CODEC
		)

	val ENTITY_SNAPSHOT: DeferredHolder<DataComponentType<*>, DataComponentType<EntitySnapshot>> =
		register(
			"entity_snapshot",
			EntitySnapshot.CODEC,
			EntitySnapshot.STREAM_CODEC
		)

}