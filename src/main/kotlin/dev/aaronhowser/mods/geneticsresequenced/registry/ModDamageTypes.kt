package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.core.registries.Registries
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModDamageTypes {

	val DAMAGE_TYPE_REGISTRY: DeferredRegister<DamageType> =
		DeferredRegister.create(Registries.DAMAGE_TYPE, GeneticsResequenced.MOD_ID)

	val VIRUS: RegistryObject<DamageType> =
		DAMAGE_TYPE_REGISTRY.register(
			"virus",
			Supplier { DamageType("virus", DamageScaling.NEVER, 0f) }
		)

}