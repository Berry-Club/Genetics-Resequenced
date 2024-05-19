package dev.aaronhowser.mods.geneticsresequenced.potions

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects.ModEffects
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate
import thedarkcolour.kotlinforforge.forge.registerObject

object ModPotions {

    val POTION_REGISTRY: DeferredRegister<Potion> =
        DeferredRegister.create(ForgeRegistries.POTIONS, GeneticsResequenced.ID)

    //TODO: Remove splash, lingering, and arrow potions (apparently needs mixins >:( )
    //TODO: Recipes
    val SUBSTRATE by register("substrate", ModEffects.SUBSTRATE)
    val CELL_GROWTH by register("cell_growth", ModEffects.CELL_GROWTH)
    val MUTATION by register("mutation", ModEffects.MUTATION)
    val VIRAL_AGENTS by register("viral_agents", ModEffects.VIRAL_AGENTS)
    val THE_CURE by register("the_cure", ModEffects.THE_CURE)

    private fun register(
        name: String,
        effect: MobEffect
    ): ObjectHolderDelegate<Potion> {
        val potion = POTION_REGISTRY
            .registerObject(name) { Potion(GeneticsResequenced.ID + "." + name, MobEffectInstance(effect)) }
        return potion
    }

}