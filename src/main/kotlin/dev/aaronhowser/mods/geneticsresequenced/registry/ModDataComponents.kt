package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.components.*
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(GeneticsResequenced.ID)

    val ENTITY_TYPE_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<EntityTypeItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("entity_type") {
            it
                .persistent(EntityTypeItemComponent.CODEC)
                .networkSynchronized(EntityTypeItemComponent.STREAM_CODEC)
        }

    val SPECIFIC_ENTITY_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<SpecificEntityItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("specific_entity") {
            it
                .persistent(SpecificEntityItemComponent.CODEC)
                .networkSynchronized(SpecificEntityItemComponent.STREAM_CODEC)
        }

    val IS_ACTIVE_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<BooleanItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("active") {
            it
                .persistent(BooleanItemComponent.CODEC)
                .networkSynchronized(BooleanItemComponent.STREAM_CODEC)
        }

    val IS_CONTAMINATED_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<BooleanItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("contaminated") {
            it
                .persistent(BooleanItemComponent.CODEC)
                .networkSynchronized(BooleanItemComponent.STREAM_CODEC)
        }

    val IS_INFINITY_ARROW: DeferredHolder<DataComponentType<*>, DataComponentType<BooleanItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("is_infinity_arrow") {
            it
                .persistent(BooleanItemComponent.CODEC)
                .networkSynchronized(BooleanItemComponent.STREAM_CODEC)
        }

    val GENES_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<GeneListItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("genes") {
            it
                .persistent(GeneListItemComponent.CODEC)
                .networkSynchronized(GeneListItemComponent.STREAM_CODEC)
        }

    val ANTIGENES_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<GeneListItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("antigenes") {
            it
                .persistent(GeneListItemComponent.CODEC)
                .networkSynchronized(GeneListItemComponent.STREAM_CODEC)
        }

    val PLASMID_PROGRESS_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<PlasmidProgressItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("plasmid_progress") {
            it
                .persistent(PlasmidProgressItemComponent.CODEC)
                .networkSynchronized(PlasmidProgressItemComponent.STREAM_CODEC)
        }

    val GENE_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<GeneItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("gene") {
            it
                .persistent(GeneItemComponent.CODEC)
                .networkSynchronized(GeneItemComponent.STREAM_CODEC)
        }

}