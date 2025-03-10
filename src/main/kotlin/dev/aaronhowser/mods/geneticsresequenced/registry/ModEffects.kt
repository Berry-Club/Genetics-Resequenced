package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.effect.BleedEffect
import dev.aaronhowser.mods.geneticsresequenced.effect.DoNothingEffect
import dev.aaronhowser.mods.geneticsresequenced.effect.PanaceaEffect
import dev.aaronhowser.mods.geneticsresequenced.effect.ZombifyVillagerEffect
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffect
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModEffects {

    val EFFECT_REGISTRY: DeferredRegister<MobEffect> =
        DeferredRegister.create(Registries.MOB_EFFECT, GeneticsResequenced.ID)

    val BLEED: DeferredHolder<MobEffect, BleedEffect> =
        EFFECT_REGISTRY.register("bleed", Supplier { BleedEffect() })

    val SUBSTRATE: DeferredHolder<MobEffect, DoNothingEffect> =
        EFFECT_REGISTRY.register("substrate", Supplier { DoNothingEffect(0x17661e) })
    val CELL_GROWTH: DeferredHolder<MobEffect, DoNothingEffect> =
        EFFECT_REGISTRY.register("cell_growth", Supplier { DoNothingEffect(0x95eb34) })
    val MUTATION: DeferredHolder<MobEffect, DoNothingEffect> =
        EFFECT_REGISTRY.register("mutation", Supplier { DoNothingEffect(0x5c0d30) })
    val VIRAL_AGENTS: DeferredHolder<MobEffect, DoNothingEffect> =
        EFFECT_REGISTRY.register("viral_agents", Supplier { DoNothingEffect(0xd18e1b, true) })

    val PANACEA: DeferredHolder<MobEffect, PanaceaEffect> =
        EFFECT_REGISTRY.register("panacea", Supplier { PanaceaEffect() })
    val ZOMBIFY_VILLAGER: DeferredHolder<MobEffect, ZombifyVillagerEffect> =
        EFFECT_REGISTRY.register("zombify_villager", Supplier { ZombifyVillagerEffect() })

}