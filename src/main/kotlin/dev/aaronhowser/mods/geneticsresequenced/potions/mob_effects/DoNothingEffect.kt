package dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class DoNothingEffect(
    name: String,
    color: Int,
    isBad: Boolean = false
) : MobEffect(
    if (isBad) MobEffectCategory.HARMFUL else MobEffectCategory.BENEFICIAL,
    color
) {

    override fun isInstantenous(): Boolean = true
    override fun isDurationEffectTick(pDuration: Int, pAmplifier: Int): Boolean = true

    override fun applyEffectTick(pLivingEntity: LivingEntity, pAmplifier: Int) {
        pLivingEntity.removeEffect(this)
    }

    override fun applyInstantenousEffect(
        pSource: Entity?,
        pIndirectSource: Entity?,
        pLivingEntity: LivingEntity,
        pAmplifier: Int,
        pHealth: Double
    ) {
        pLivingEntity.removeEffect(this)
    }

}