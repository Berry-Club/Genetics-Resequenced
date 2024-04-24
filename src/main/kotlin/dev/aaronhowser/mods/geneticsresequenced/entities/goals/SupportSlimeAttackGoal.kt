package dev.aaronhowser.mods.geneticsresequenced.entities.goals

import dev.aaronhowser.mods.geneticsresequenced.entities.SupportSlime
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.Goal
import kotlin.math.max

class SupportSlimeAttackGoal(
    private val mob: SupportSlime
) : Goal() {
    private var ticksUntilNextAttack = 0
    private var lastCanUseCheck: Long = 0

    override fun canUse(): Boolean {
        val currentTime = mob.level.gameTime
        if (currentTime - lastCanUseCheck < 20) {
            return false
        }
        lastCanUseCheck = currentTime

        val target = mob.target
        if (target == null || !target.isAlive) {
            return false
        }

        val distanceToSqr = mob.distanceToSqr(target)
        return getAttackReachSqr(target) >= distanceToSqr
    }

    override fun start() {
        val target = mob.target as? Entity ?: return
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
        mob.lookControl.setLookAt(target, 30.0f, 30.0f)
        val distanceSqr = mob.distanceToSqr(target.x, target.y, target.z)

        this.ticksUntilNextAttack = max(
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

    private fun resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(20)
    }

    private fun getAttackReachSqr(pAttackTarget: LivingEntity): Double {
        return (mob.bbWidth * 2.0f * mob.bbWidth * 2.0f + pAttackTarget.bbWidth).toDouble()
    }
}