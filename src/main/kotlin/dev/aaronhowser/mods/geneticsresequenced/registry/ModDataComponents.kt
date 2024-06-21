package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeComponent
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
                .persistent(EntityTypeComponent.CODEC)
                .networkSynchronized(EntityTypeComponent.STREAM_CODEC)
                .build()
        })

}