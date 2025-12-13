package dev.aaronhowser.mods.geneticsresequenced.entity.goals

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal

// https://github.com/Elenterius/Biomancy/blob/mc1.20.1/prod/src/main/java/com/github/elenterius/biomancy/entity/mob/ai/goal/FrenzyMeleeAttackGoal.java
class FrenzyMeleeAttackGoal(
	mob: PathfinderMob,
	speedModifier: Double,
	followingTargetEvenIfNotSeen: Boolean
) : MeleeAttackGoal(mob, speedModifier, followingTargetEvenIfNotSeen) {

	override fun canUse(): Boolean {
		val frenzyGene = ModGenes.FRENZIED.getHolderOrThrow(mob.registryAccess())
		if (frenzyGene.isDisabled) return false

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