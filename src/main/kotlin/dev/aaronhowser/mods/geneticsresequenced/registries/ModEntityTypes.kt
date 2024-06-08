package dev.aaronhowser.mods.geneticsresequenced.registries

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.entities.SupportSlime
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModEntityTypes {

    val ENTITY_TYPE_REGISTRY: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GeneticsResequenced.ID)

    val SUPPORT_SLIME: RegistryObject<EntityType<SupportSlime>> = ENTITY_TYPE_REGISTRY.register("support_slime") {
        EntityType.Builder.of(
            { type, world -> SupportSlime(type, world) },
            MobCategory.CREATURE
        )
            .sized(0.75f, 0.75f)
            .build("support_slime")
    }
}