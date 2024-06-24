package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModEntityTypes {

    val ENTITY_TYPE_REGISTRY: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, GeneticsResequenced.ID)

    val SUPPORT_SLIME: DeferredHolder<EntityType<*>, EntityType<SupportSlime>> = ENTITY_TYPE_REGISTRY.register("support_slime", Supplier {
        EntityType.Builder.of(
            { type, level -> SupportSlime(type, level) },
            MobCategory.CREATURE
        )
            .sized(0.75f, 0.75f)
            .build("support_slime")
    })

}