package dev.aaronhowser.mods.geneticsresequenced.effect

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

class BleedEffect : MobEffect(
    MobEffectCategory.HARMFUL,
    0x5c0d30
) {

    override fun shouldApplyEffectTickThisTick(pDuration: Int, pAmplifier: Int): Boolean {
        return pDuration % 20 == 0
    }


    override fun applyEffectTick(pLivingEntity: LivingEntity, pAmplifier: Int): Boolean {
        if (pLivingEntity.level().isClientSide) return false

        pLivingEntity.hurt(
            DamageSource(getHolder(pLivingEntity.level())),
            1f
        )

        return true
    }

    companion object {
        val BLEED_DAMAGE: ResourceKey<DamageType> =
            ResourceKey.create(Registries.DAMAGE_TYPE, OtherUtil.modResource("bleed"))

        fun getHolder(level: Level): Holder.Reference<DamageType> {
            return level.registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(BLEED_DAMAGE)
        }
    }

}