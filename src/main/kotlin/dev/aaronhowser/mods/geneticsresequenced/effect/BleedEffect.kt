package dev.aaronhowser.mods.geneticsresequenced.effect

import dev.aaronhowser.mods.geneticsresequenced.datagen.datapack.ModDamageTypeProvider
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

class BleedEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0x5c0d30
) {

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
		return duration % 20 == 0
	}

	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		if (livingEntity.level().isClientSide) return false

		livingEntity.hurt(
			livingEntity.damageSources().source(ModDamageTypeProvider.BLEED),
			1f
		)

		return true
	}

}