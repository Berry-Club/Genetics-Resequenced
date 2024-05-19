package dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.effect.MobEffect
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

@Suppress("unused")
object ModEffects {

    val EFFECT_REGISTRY: DeferredRegister<MobEffect> =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GeneticsResequenced.ID)

    val BLEED by register("bleed") { BleedEffect() }
    val SUBSTRATE by register("substrate") { DoNothingEffect("substrate", 0x00FF00) }
    val CELL_GROWTH by register("cell_growth") { DoNothingEffect("cell_growth", 0x00FF00) }
    val MUTATION by register("mutation") { DoNothingEffect("mutation", 0x00FF00) }
    val VIRAL_AGENTS by register("viral_agents") { DoNothingEffect("viral_agents", 0x440000, true) }
    val THE_CURE by register("the_cure") { DoNothingEffect("the_cure", 0x0000FF) }

    private fun register(
        name: String,
        supplier: () -> MobEffect
    ): ObjectHolderDelegate<MobEffect> {
        return EFFECT_REGISTRY.registerObject(name, supplier)
    }

}