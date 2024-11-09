package dev.aaronhowser.mods.geneticsresequenced.registry

import com.mojang.serialization.Codec
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

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, GeneticsResequenced.ID)

    val ENTITY_TYPE_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<EntityType<*>>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("entity_type") {
            it
                .persistent(BuiltInRegistries.ENTITY_TYPE.byNameCodec())
                .networkSynchronized(ByteBufCodecs.registry(Registries.ENTITY_TYPE))
        }

    val SPECIFIC_ENTITY_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<SpecificEntityItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("specific_entity") {
            it
                .persistent(SpecificEntityItemComponent.CODEC)
                .networkSynchronized(SpecificEntityItemComponent.STREAM_CODEC)
        }

    private fun boolCodec(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        DATA_COMPONENT_REGISTRY.registerComponentType(name) {
            it
                .persistent(Codec.BOOL)
                .networkSynchronized(ByteBufCodecs.BOOL)
        }

    val IS_ACTIVE_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        boolCodec("is_active")

    val IS_CONTAMINATED_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        boolCodec("is_contaminated")

    val IS_INFINITY_ARROW: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
        boolCodec("is_infinity_arrow")

    private fun geneListComponent(name: String): DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
        DATA_COMPONENT_REGISTRY.registerComponentType(name) {
            it
                .persistent(Gene.HOLDER_SET_CODEC)
                .networkSynchronized(Gene.HOLDER_SET_STREAM_CODEC)
        }

    val GENES_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
        geneListComponent("genes")

    val ANTIGENES_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<HolderSet<Gene>>> =
        geneListComponent("antigenes")

    val PLASMID_PROGRESS_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<PlasmidProgressItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("plasmid_progress") {
            it
                .persistent(PlasmidProgressItemComponent.CODEC)
                .networkSynchronized(PlasmidProgressItemComponent.STREAM_CODEC)
        }

    val GENE_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<Holder<Gene>>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("gene") {
            it
                .persistent(Gene.CODEC)
                .networkSynchronized(Gene.STREAM_CODEC)
        }

}