package dev.aaronhowser.mods.geneticsresequenced.registry

import com.mojang.serialization.Codec
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

	val ENTITY_TYPE: DeferredHolder<DataComponentType<*>, DataComponentType<EntityType<*>>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("entity_type") {
			it
				.persistent(BuiltInRegistries.ENTITY_TYPE.byNameCodec())
				.networkSynchronized(ByteBufCodecs.registry(Registries.ENTITY_TYPE))
		}

	val SPECIFIC_ENTITY: DeferredHolder<DataComponentType<*>, DataComponentType<SpecificEntityItemComponent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("specific_entity") {
			it
				.persistent(SpecificEntityItemComponent.CODEC)
				.networkSynchronized(SpecificEntityItemComponent.STREAM_CODEC)
		}

	val IS_ACTIVE: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		boolean("is_active")

	val IS_CONTAMINATED: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		boolean("is_contaminated")

	val IS_INFINITY_ARROW: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		boolean("is_infinity_arrow")

	val DRAGON_HEALTH_CRYSTAL_DAMAGE: DeferredHolder<DataComponentType<*>, DataComponentType<Float>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("dragon_health_crystal_damage") {
			it
				.persistent(Codec.FLOAT)
				.networkSynchronized(ByteBufCodecs.FLOAT)
		}

	val GENE: DeferredHolder<DataComponentType<*>, DataComponentType<Holder<Gene>>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("gene") {
			it
				.persistent(Gene.CODEC)
				.networkSynchronized(Gene.STREAM_CODEC)
		}

	val GENE_SET: DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
		geneList("genes")

	val ANTIGENE_SET: DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
		geneList("antigenes")

	val PLASMID_PROGRESS: DeferredHolder<DataComponentType<*>, DataComponentType<PlasmidProgressItemComponent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("plasmid_progress") {
			it
				.persistent(PlasmidProgressItemComponent.CODEC)
				.networkSynchronized(PlasmidProgressItemComponent.STREAM_CODEC)
		}

	private fun geneList(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
		DATA_COMPONENT_REGISTRY.registerComponentType(name) {
			it
				.persistent(Gene.HOLDER_SET_CODEC)
				.networkSynchronized(Gene.HOLDER_SET_STREAM_CODEC)
		}

	private fun boolean(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		DATA_COMPONENT_REGISTRY.registerComponentType(name) {
			it
				.persistent(Codec.BOOL)
				.networkSynchronized(ByteBufCodecs.BOOL)
		}

}