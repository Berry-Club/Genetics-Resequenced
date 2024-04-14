package dev.aaronhowser.mods.geneticsresequenced.effects

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.effect.MobEffect
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

@Suppress("unused")
object ModEffects {

    val EFFECT_REGISTRY: DeferredRegister<MobEffect> =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, GeneticsResequenced.ID)

    val BLEED by EFFECT_REGISTRY.registerObject("bleed") { BleedEffect() }

}