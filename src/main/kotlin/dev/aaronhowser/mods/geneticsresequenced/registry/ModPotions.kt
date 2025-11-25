package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModPotions {

	val POTION_REGISTRY: DeferredRegister<Potion> =
		DeferredRegister.create(ForgeRegistries.POTIONS, GeneticsResequenced.MOD_ID)

	val SUBSTRATE: RegistryObject<Potion> =
		register("substrate", ModEffects.SUBSTRATE)
	val CELL_GROWTH: RegistryObject<Potion> =
		register("cell_growth", ModEffects.CELL_GROWTH)
	val MUTATION: RegistryObject<Potion> =
		register("mutation", ModEffects.MUTATION)
	val VIRAL_AGENTS: RegistryObject<Potion> =
		register("viral_agents", ModEffects.VIRAL_AGENTS)

	val PANACEA: RegistryObject<Potion> =
		register("panacea", ModEffects.PANACEA)
	val ZOMBIFY_VILLAGER: RegistryObject<Potion> =
		register("zombify_villager", ModEffects.ZOMBIFY_VILLAGER)

	private fun register(
		id: String,
		effect: RegistryObject<out MobEffect>
	): RegistryObject<Potion> {
		val potionId = "${GeneticsResequenced.MOD_ID}.$id"  // Required for localization

		return POTION_REGISTRY.register(id, Supplier { Potion(potionId, MobEffectInstance(effect.get())) })
	}

}