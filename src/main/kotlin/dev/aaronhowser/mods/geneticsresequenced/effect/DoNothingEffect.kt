package dev.aaronhowser.mods.geneticsresequenced.effect

import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class DoNothingEffect(
    color: Int,
    isBad: Boolean = false,
    private val removeImmediately: Boolean = true
) : MobEffect(
    if (isBad) MobEffectCategory.HARMFUL else MobEffectCategory.NEUTRAL,
    color
) {

    override fun isInstantenous(): Boolean = removeImmediately

    override fun applyInstantenousEffect(
        pSource: Entity?,
        pIndirectSource: Entity?,
        pLivingEntity: LivingEntity,
        pAmplifier: Int,
        pHealth: Double
    ) {
        if (removeImmediately) pLivingEntity.removeEffect(Holder.direct(this))
    }

}