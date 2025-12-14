package dev.aaronhowser.mods.geneticsresequenced.entity.goals

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal

// https://github.com/Elenterius/Biomancy/blob/mc1.20.1/prod/src/main/java/com/github/elenterius/biomancy/entity/mob/ai/goal/FrenzyAttackableTargetGoal.java
class FrenzyTargetGoal<T : LivingEntity> : NearestAttackableTargetGoal<T> {

	constructor(goalOwner: Mob, targetClass: Class<T>) : super(
		goalOwner,
		targetClass,
		true
	)

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

}