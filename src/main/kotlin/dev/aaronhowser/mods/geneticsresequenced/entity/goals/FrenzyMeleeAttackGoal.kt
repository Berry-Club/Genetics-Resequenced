package dev.aaronhowser.mods.geneticsresequenced.entity.goals

import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.item.enchantment.EnchantmentHelper

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

	override fun checkAndPerformAttack(pEnemy: LivingEntity, pDistToEnemySqr: Double) {
		val hasAttackDamageAttribute = mob.getAttribute(Attributes.ATTACK_DAMAGE) != null
		if (hasAttackDamageAttribute) {
			super.checkAndPerformAttack(pEnemy, pDistToEnemySqr)
			return
		}

		val distSqr = mob.distanceToSqr(pEnemy)
		val reachSqr = getAttackReachSqr(pEnemy)

		if (distSqr <= reachSqr && ticksUntilNextAttack <= 0) {
			resetAttackCooldown()
			mob.swing(InteractionHand.MAIN_HAND)
			attackWithoutAttribute(mob, pEnemy)
		}
	}

	companion object {
		private fun attackWithoutAttribute(attacker: PathfinderMob, target: LivingEntity) {
			var damage = 3f

			val level = attacker.level()
			val damageSource = attacker.damageSources().mobAttack(attacker)

			if (level is ServerLevel) {
				damage = EnchantmentHelper.getDamageBonus(attacker.mainHandItem, attacker.mobType)
			}

			val hit = target.hurt(damageSource, damage)

			if (hit) {
				//FIXME
//				val knockback = attacker.getKnockback(target, damageSource)
//				if (knockback > 0.0f) {
//					val xKnock = sin(attacker.yRot * (Math.PI / 180.0)).toFloat()
//					val zKnock = -cos(attacker.yRot * (Math.PI / 180.0)).toFloat()
//					target.knockback((knockback * 0.5f).toDouble(), xKnock.toDouble(), zKnock.toDouble())
//					attacker.deltaMovement = attacker.deltaMovement.multiply(0.6, 1.0, 0.6)
//				}

				if (level is ServerLevel) {
					EnchantmentHelper.doPostHurtEffects(attacker, target)
				}

				attacker.setLastHurtMob(target)
			}
		}

	}

}