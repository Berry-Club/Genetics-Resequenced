package dev.aaronhowser.mods.geneticsresequenced.registries

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.mob_effects.BleedEffect
import dev.aaronhowser.mods.geneticsresequenced.mob_effects.DoNothingEffect
import dev.aaronhowser.mods.geneticsresequenced.mob_effects.TheCureEffect
import dev.aaronhowser.mods.geneticsresequenced.mob_effects.ZombifyVillagerEffect
import net.minecraft.world.effect.MobEffect
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

@Suppress("unused")
object ModEffects {

    val EFFECT_REGISTRY: DeferredRegister<MobEffect> =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GeneticsResequenced.ID)

    val BLEED: MobEffect by register("bleed") { BleedEffect() }
    val SUBSTRATE: MobEffect by register("substrate") { DoNothingEffect("substrate", 0x17661e) }
    val CELL_GROWTH: MobEffect by register("cell_growth") { DoNothingEffect("cell_growth", 0x95eb34) }
    val MUTATION: MobEffect by register("mutation") { DoNothingEffect("mutation", 0x5c0d30) }
    val VIRAL_AGENTS: MobEffect by register("viral_agents") { DoNothingEffect("viral_agents", 0xd18e1b, isBad = true) }
    val SILLY: MobEffect by register("silly") { DoNothingEffect("silly", 0xe4fd01, removeImmediately = false) }
    val THE_CURE: MobEffect by register("the_cure") { TheCureEffect() }

    val ZOMBIFY_VILLAGER by register("zombify_villager") { ZombifyVillagerEffect() }

    private fun register(
        name: String,
        supplier: () -> MobEffect
    ): ObjectHolderDelegate<MobEffect> {
        return EFFECT_REGISTRY.registerObject(name, supplier)
    }

}