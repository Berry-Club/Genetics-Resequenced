package dev.aaronhowser.mods.geneticsresequenced.entity.goals

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.item.enchantment.EnchantmentHelper
import kotlin.math.cos
import kotlin.math.sin

// https://github.com/Elenterius/Biomancy/blob/mc1.20.1/prod/src/main/java/com/github/elenterius/biomancy/entity/mob/ai/goal/FrenzyMeleeAttackGoal.java
class FrenzyMeleeAttackGoal(
	mob: PathfinderMob,
	speedModifier: Double,
	followingTargetEvenIfNotSeen: Boolean
) : MeleeAttackGoal(mob, speedModifier, followingTargetEvenIfNotSeen) {

	override fun canUse(): Boolean {
		return super.canUse() && mob.hasGene(ModGenes.FRENZIED)
	}

	override fun canContinueToUse(): Boolean {
		if (!mob.hasGene(ModGenes.FRENZIED)) {
			stop()
			return false
		}

		return super.canContinueToUse()
	}

	override fun checkAndPerformAttack(target: LivingEntity) {
		val hasAttackDamageAttribute = mob.getAttribute(Attributes.ATTACK_DAMAGE) != null
		if (hasAttackDamageAttribute) {
			super.checkAndPerformAttack(target)
			return
		}

		if (canPerformAttack(target)) {
			resetAttackCooldown()
			mob.swing(InteractionHand.MAIN_HAND)
			attackWithoutAttribute(mob, target)
		}
	}

	companion object {
		private fun attackWithoutAttribute(attacker: PathfinderMob, target: LivingEntity) {
			var damage = 3f

			val level = attacker.level()
			if (level !is ServerLevel) return

			val damageSource = level.damageSources().mobAttack(attacker)

			damage = EnchantmentHelper.modifyDamage(level, attacker.weaponItem, target, damageSource, damage)

			val flag = target.hurtServer(level, damageSource, damage)

			if (flag) {
				val knockback = attacker.getKnockback(target, damageSource)
				if (knockback > 0.0f) {
					target.knockback(
						(knockback * 0.5f).toDouble(),
						sin(attacker.yRot * (Math.PI / 180.0).toFloat()).toDouble(),
						(-cos(attacker.yRot * (Math.PI / 180.0).toFloat())).toDouble()
					)

					attacker.deltaMovement = attacker.deltaMovement.multiply(0.6, 1.0, 0.6)
				}

				EnchantmentHelper.doPostAttackEffects(level, target, damageSource)

				attacker.setLastHurtMob(target)
				attacker.playAttackSound()
			}
		}
	}

}
