package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModPotions {

    val POTION_REGISTRY: DeferredRegister<Potion> =
        DeferredRegister.create(Registries.POTION, GeneticsResequenced.ID)

    val SUBSTRATE: DeferredHolder<Potion, Potion> =
        register("substrate", ModEffects.SUBSTRATE)
    val CELL_GROWTH: DeferredHolder<Potion, Potion> =
        register("cell_growth", ModEffects.CELL_GROWTH)
    val MUTATION: DeferredHolder<Potion, Potion> =
        register("mutation", ModEffects.MUTATION)
    val VIRAL_AGENTS: DeferredHolder<Potion, Potion> =
        register("viral_agents", ModEffects.VIRAL_AGENTS)

    val PANACEA: DeferredHolder<Potion, Potion> =
        register("panacea", ModEffects.PANACEA)
    val ZOMBIFY_VILLAGER: DeferredHolder<Potion, Potion> =
        register("zombify_villager", ModEffects.ZOMBIFY_VILLAGER)

    private fun register(id: String, effect: DeferredHolder<MobEffect, out MobEffect>): DeferredHolder<Potion, Potion> {
        val potionId = "${GeneticsResequenced.ID}.$id"  // Required for localization

        return POTION_REGISTRY.register(id, Supplier { Potion(potionId, MobEffectInstance(effect)) })
    }

}