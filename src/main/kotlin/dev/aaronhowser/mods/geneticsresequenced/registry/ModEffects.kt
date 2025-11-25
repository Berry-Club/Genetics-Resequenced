package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.effect.BleedEffect
import dev.aaronhowser.mods.geneticsresequenced.effect.DoNothingEffect
import dev.aaronhowser.mods.geneticsresequenced.effect.PanaceaEffect
import dev.aaronhowser.mods.geneticsresequenced.effect.ZombifyVillagerEffect
import net.minecraft.world.effect.MobEffect
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModEffects {

	val EFFECT_REGISTRY: DeferredRegister<MobEffect> =
		DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GeneticsResequenced.ID)

	val BLEED: RegistryObject<BleedEffect> =
		EFFECT_REGISTRY.register("bleed", Supplier { BleedEffect() })

	val SUBSTRATE: RegistryObject<DoNothingEffect> =
		EFFECT_REGISTRY.register("substrate", Supplier { DoNothingEffect(0x17661e) })
	val CELL_GROWTH: RegistryObject<DoNothingEffect> =
		EFFECT_REGISTRY.register("cell_growth", Supplier { DoNothingEffect(0x95eb34) })
	val MUTATION: RegistryObject<DoNothingEffect> =
		EFFECT_REGISTRY.register("mutation", Supplier { DoNothingEffect(0x5c0d30) })
	val VIRAL_AGENTS: RegistryObject<DoNothingEffect> =
		EFFECT_REGISTRY.register("viral_agents", Supplier { DoNothingEffect(0xd18e1b, isBad = true) })

	val PANACEA: RegistryObject<PanaceaEffect> =
		EFFECT_REGISTRY.register("panacea", Supplier { PanaceaEffect() })
	val ZOMBIFY_VILLAGER: RegistryObject<ZombifyVillagerEffect> =
		EFFECT_REGISTRY.register("zombify_villager", Supplier { ZombifyVillagerEffect() })

}