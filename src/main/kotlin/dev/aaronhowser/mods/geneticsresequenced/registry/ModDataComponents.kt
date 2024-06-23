package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.GenesItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister<DataComponentType<*>> =
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, GeneticsResequenced.ID)

    val ENTITY_TYPE_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<EntityTypeItemComponent>> =
        DATA_COMPONENT_REGISTRY.register("entity_type", Supplier {
            DataComponentType.builder<EntityTypeItemComponent>()
                .persistent(EntityTypeItemComponent.CODEC)
                .networkSynchronized(EntityTypeItemComponent.STREAM_CODEC)
                .build()
        })

    val SPECIFIC_ENTITY_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<SpecificEntityItemComponent>> =
        DATA_COMPONENT_REGISTRY.register("specific_entity", Supplier {
            DataComponentType.builder<SpecificEntityItemComponent>()
                .persistent(SpecificEntityItemComponent.CODEC)
                .networkSynchronized(SpecificEntityItemComponent.STREAM_CODEC)
                .build()
        })

    val BOOLEAN_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<BooleanItemComponent>> =
        DATA_COMPONENT_REGISTRY.register("boolean", Supplier {
            DataComponentType.builder<BooleanItemComponent>()
                .persistent(BooleanItemComponent.CODEC)
                .networkSynchronized(BooleanItemComponent.STREAM_CODEC)
                .build()
        })

    val GENES_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<GenesItemComponent>> =
        DATA_COMPONENT_REGISTRY.register("genes", Supplier {
            DataComponentType.builder<GenesItemComponent>()
                .persistent(GenesItemComponent.CODEC)
                .networkSynchronized(GenesItemComponent.STREAM_CODEC)
                .build()
        })

}