package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.entity.SupportSlime
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModEntityTypes {

	val ENTITY_TYPE_REGISTRY: DeferredRegister<EntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, GeneticsResequenced.MOD_ID)

	val SUPPORT_SLIME: DeferredHolder<EntityType<*>, EntityType<SupportSlime>> = ENTITY_TYPE_REGISTRY.register("support_slime", Supplier {
		val key: ResourceKey<EntityType<*>> = ResourceKey.create(Registries.ENTITY_TYPE, GeneticsResequenced.modResource("support_slime"))
		EntityType.Builder.of(
			{ type, level -> SupportSlime(type, level) },
			MobCategory.CREATURE
		)
			.sized(0.75f, 0.75f)
			.build(key)
	})

}
