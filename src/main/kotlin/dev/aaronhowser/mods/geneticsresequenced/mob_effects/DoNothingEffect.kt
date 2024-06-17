package dev.aaronhowser.mods.geneticsresequenced.mob_effects

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class DoNothingEffect(
    name: String,
    color: Int,
    isBad: Boolean = false,
    private val removeImmediately: Boolean = true
) : MobEffect(
    if (isBad) MobEffectCategory.HARMFUL else MobEffectCategory.BENEFICIAL,
    color
) {

    override fun isInstantenous(): Boolean = removeImmediately
    override fun isDurationEffectTick(pDuration: Int, pAmplifier: Int): Boolean = removeImmediately

    override fun applyEffectTick(pLivingEntity: LivingEntity, pAmplifier: Int) {
        if (removeImmediately) pLivingEntity.removeEffect(this)
    }

    override fun applyInstantenousEffect(
        pSource: Entity?,
        pIndirectSource: Entity?,
        pLivingEntity: LivingEntity,
        pAmplifier: Int,
        pHealth: Double
    ) {
        if (removeImmediately) pLivingEntity.removeEffect(this)
    }

}