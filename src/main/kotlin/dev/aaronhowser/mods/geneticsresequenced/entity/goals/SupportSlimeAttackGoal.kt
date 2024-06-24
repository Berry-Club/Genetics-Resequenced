package dev.aaronhowser.mods.geneticsresequenced.entity.goals

import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.Goal

class SupportSlimeAttackGoal(
    private val mob: SupportSlime
) : Goal() {

    private var ticksUntilNextAttack = 0

    override fun canUse(): Boolean = true

    override fun start() {
        val target = mob.target

        if (target == null) {
            mob.isAggressive = false
            return
        }

        mob.isAggressive = true
        mob.navigation.moveTo(target, 1.0)
        ticksUntilNextAttack = 0
    }

    override fun stop() {
        mob.isAggressive = false
        mob.target = null
        mob.navigation.stop()
    }

    override fun tick() {
        val target = mob.target ?: return
        mob.lookControl.setLookAt(target)

        val distanceSqr = mob.distanceToSqr(target)

        this.ticksUntilNextAttack = maxOf(
            ticksUntilNextAttack - 1,
            0
        )

        this.checkAndPerformAttack(target, distanceSqr)
    }


    private fun checkAndPerformAttack(pEnemy: LivingEntity, pDistToEnemySqr: Double) {
        val d0: Double = this.getAttackReachSqr(pEnemy)
        if (pDistToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown()
            this.mob.doHurtTarget(pEnemy)
        }
    }

    private fun getAttackReachSqr(pAttackTarget: LivingEntity): Double {
        return (mob.bbWidth * 2.0f * mob.bbWidth * 2.0f + pAttackTarget.bbWidth).toDouble()
    }

    private fun resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20)
    }

}