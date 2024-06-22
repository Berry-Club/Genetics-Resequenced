package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.codec.ModCodecs
import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.GenesComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister<DataComponentType<*>> =
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, GeneticsResequenced.ID)

    val ENTITY_TYPE_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<EntityTypeComponent>> =
        DATA_COMPONENT_REGISTRY.register("entity_type", Supplier {
            DataComponentType.builder<EntityTypeComponent>()
                .persistent(ModCodecs.entityType)
                .networkSynchronized(ModCodecs.entityType_stream)
                .build()
        })

    val SPECIFIC_ENTITY_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<SpecificEntityComponent>> =
        DATA_COMPONENT_REGISTRY.register("specific_entity", Supplier {
            DataComponentType.builder<SpecificEntityComponent>()
                .persistent(SpecificEntityComponent.CODEC)
                .networkSynchronized(SpecificEntityComponent.STREAM_CODEC)
                .build()
        })

    val GENES_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<GenesComponent>> =
        DATA_COMPONENT_REGISTRY.register("genes", Supplier {
            DataComponentType.builder<GenesComponent>()
                .persistent(GenesComponent.CODEC)
                .networkSynchronized(GenesComponent.STREAM_CODEC)
                .build()
        })

    val BOOLEAN_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<BooleanComponent>> =
        DATA_COMPONENT_REGISTRY.register("boolean", Supplier {
            DataComponentType.builder<BooleanComponent>()
                .persistent(ModCodecs.boolean)
                .networkSynchronized()
                .build()
        })

}