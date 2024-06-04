package dev.aaronhowser.mods.geneticsresequenced.potions.mob_effects

import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

class BleedEffect : MobEffect(
    MobEffectCategory.HARMFUL,
    0x5c0d30
) {

    override fun isDurationEffectTick(pDuration: Int, pAmplifier: Int): Boolean {
        return pDuration % 20 == 0
    }

    override fun applyEffectTick(pLivingEntity: LivingEntity, pAmplifier: Int) {
        if (pLivingEntity.level.isClientSide) return
        pLivingEntity.hurt(DamageSource.MAGIC, ServerConfig.clawsDamage.get().toFloat())
        super.applyEffectTick(pLivingEntity, pAmplifier)
    }

}