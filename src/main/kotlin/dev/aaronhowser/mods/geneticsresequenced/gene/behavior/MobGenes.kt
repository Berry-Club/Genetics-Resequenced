package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.registryAccess
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.entity.goals.FrenzyMeleeAttackGoal
import dev.aaronhowser.mods.geneticsresequenced.entity.goals.FrenzyTargetGoal
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.Bee
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent
import kotlin.jvm.optionals.getOrNull

object MobGenes {

	private var isFertileCloning = false
	fun handleFertile(event: BabyEntitySpawnEvent) {
		if (isFertileCloning) return

		val parentA = event.parentA as? Animal ?: return
		val parentB = event.parentB as? Animal ?: return

		val level = parentA.level() as? ServerLevel ?: return

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
	fun shouldPlacidCancelTargetGoal(goal: TargetGoal): Boolean {
		return goal.mob.hasGene(ModGenes.PLACID)
	}

	// https://github.com/Elenterius/Biomancy/blob/mc1.20.1/prod/src/main/java/com/github/elenterius/biomancy/serum/FrenzySerum.java#L99
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
				4,
				FrenzyMeleeAttackGoal(mob, 1.0, false)
			)
		}
	}

	@JvmStatic
	fun beeRequiredPollinationTime(bee: Bee): Int {
		if (bee.hasGene(ModGenes.BOUNTIFUL_TWO)) {
			return 100
		}

		if (bee.hasGene(ModGenes.BOUNTIFUL)) {
			return 200
		}

		return 400
	}

	fun attachScareGoals(entity: PathfinderMob) {
		val allGenes = ModGenes.getAllGeneHolders(entity.registryAccess())

		for (gene in allGenes) {
			if (gene.isDisabled) continue
			val cowardTag = gene.value().scaresEntitiesWithTag.getOrNull() ?: continue

			if (!entity.isEntity(cowardTag)) continue

			entity.goalSelector.addGoal(
				1,
				AvoidEntityGoal(
					entity,
					LivingEntity::class.java,
					{ otherEntity: LivingEntity -> otherEntity.hasGene(gene) },
					12.0f,
					1.2,
					1.6,
					EntitySelector.NO_SPECTATORS::test
				)
			)
		}
	}

}