package dev.aaronhowser.mods.genetics_resequenced.effect

import dev.aaronhowser.mods.genetics_resequenced.datagen.datapack.ModDamageTypeProvider
import net.minecraft.server.level.ServerLevel
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

	override fun applyEffectTick(serverLevel: ServerLevel, livingEntity: LivingEntity, amplifier: Int): Boolean {
		livingEntity.hurt(
			livingEntity.damageSources().source(ModDamageTypeProvider.BLEED),
			1f
		)

		return true
	}

}
