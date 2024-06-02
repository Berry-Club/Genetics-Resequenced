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
    val SUBSTRATE by register("substrate") { DoNothingEffect("substrate", 0x17661e) }
    val CELL_GROWTH by register("cell_growth") { DoNothingEffect("cell_growth", 0x95eb34) }
    val MUTATION by register("mutation") { DoNothingEffect("mutation", 0x5c0d30) }
    val VIRAL_AGENTS by register("viral_agents") { DoNothingEffect("viral_agents", 0xd18e1b, true) }
    val THE_CURE by register("the_cure") { TheCureEffect() }

    val ZOMBIFY_VILLAGER by register("zombify_villager") { ZombifyVillagerEffect() }

    private fun register(
        name: String,
        supplier: () -> MobEffect
    ): ObjectHolderDelegate<MobEffect> {
        return EFFECT_REGISTRY.registerObject(name, supplier)
    }

}