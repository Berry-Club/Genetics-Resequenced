package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.entity.goals.FrenzyMeleeAttackGoal
import dev.aaronhowser.mods.geneticsresequenced.entity.goals.FrenzyTargetGoal
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.animal.Animal
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent

object MobGenes {

	private var isFertileCloning = false
	fun handleFertile(event: BabyEntitySpawnEvent) {
		if (isFertileCloning) return

		val parentA = event.parentA as? Animal ?: return
		val parentB = event.parentB as? Animal ?: return

		val level = parentA.level() as? ServerLevel ?: return

		val fertile = ModGenes.FERTILE.getHolderOrThrow(parentA.registryAccess())
		if (fertile.isDisabled) return

		var extraBabies = 0
		if (parentA.hasGene(ModGenes.FERTILE)) extraBabies++
		if (parentB.hasGene(ModGenes.FERTILE)) extraBabies++
		if (extraBabies == 0) return

		isFertileCloning = true
		for (i in 0 until extraBabies) {
			parentA.spawnChildFromBreeding(level, parentB)
		}
		isFertileCloning = false
	}

	@JvmStatic
	fun shouldPlacidCancelGoal(goal: TargetGoal): Boolean {
		val entity = goal.mob

		val placid = ModGenes.PLACID.getHolderOrThrow(entity.registryAccess())
		if (placid.isDisabled) return false

		return entity.hasGene(ModGenes.PLACID)
	}

	fun giveFrenzyGoals(mob: Mob) {
		if (mob !is PathfinderMob) return

		val alreadyHasFrenzyTargetGoal = mob.targetSelector.availableGoals.any { it.goal is FrenzyTargetGoal<*> }
		if (!alreadyHasFrenzyTargetGoal) {
			mob.targetSelector.addGoal(
				1,
				FrenzyTargetGoal(mob, LivingEntity::class.java)
			)
		}

		val alreadyCanAttack = mob.goalSelector.availableGoals.any { it.goal is MeleeAttackGoal }
		if (!alreadyCanAttack) {
			mob.goalSelector.addGoal(
				2,
				FrenzyMeleeAttackGoal(mob, 1.0, false)
			)
		}
	}

}