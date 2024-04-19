package dev.aaronhowser.mods.geneticsresequenced.entities

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModEntityTypes {

    val ENTITY_TYPE_REGISTRY: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GeneticsResequenced.ID)

    val FRIENDLY_SLIME: RegistryObject<EntityType<FriendlySlime>> = ENTITY_TYPE_REGISTRY.register("friendly_slime") {
        EntityType.Builder.of(
            { type, world -> FriendlySlime(type, world) },
            MobCategory.CREATURE
        )
            .sized(0.75f, 0.75f)
            .build("friendly_slime")
    }
}