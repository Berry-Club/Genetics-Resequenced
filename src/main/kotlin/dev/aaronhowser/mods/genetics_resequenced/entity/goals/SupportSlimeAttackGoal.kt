package dev.aaronhowser.mods.genetics_resequenced.entity.goals

import dev.aaronhowser.mods.genetics_resequenced.entity.SupportSlime
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.Goal

class SupportSlimeAttackGoal(
	private val mob: SupportSlime
) : Goal() {

	private var ticksUntilNextAttack = 0

	override fun canUse(): Boolean = true

	override fun start() {
		val target = this.mob.target

		if (target == null) {
			this.mob.isAggressive = false
			return
		}

		this.mob.isAggressive = true
		this.mob.navigation.moveTo(target, 1.0)
		this.ticksUntilNextAttack = 0
	}

	override fun stop() {
		this.mob.isAggressive = false
		this.mob.target = null
		this.mob.navigation.stop()
	}

	override fun tick() {
		val target = this.mob.target ?: return
		this.mob.lookControl.setLookAt(target)

		val distanceSqr = this.mob.distanceToSqr(target)

		this.ticksUntilNextAttack = maxOf(
			ticksUntilNextAttack - 1,
			0
		)

		this.checkAndPerformAttack(target, distanceSqr)
	}


	private fun checkAndPerformAttack(enemy: LivingEntity, distToEnemySqr: Double) {
		val serverLevel = mob.level() as? ServerLevel ?: return

		val reachSqr = getAttackReachSqr(enemy)
		if (distToEnemySqr <= reachSqr && ticksUntilNextAttack <= 0) {
			resetAttackCooldown()
			mob.doHurtTarget(serverLevel, enemy)
		}
	}

	private fun getAttackReachSqr(pAttackTarget: LivingEntity): Double {
		return (mob.bbWidth * 2.0f * mob.bbWidth * 2.0f + pAttackTarget.bbWidth).toDouble()
	}

	private fun resetAttackCooldown() {
		this.ticksUntilNextAttack = adjustedTickDelay(20)
	}

}
