package dev.aaronhowser.mods.genetics_resequenced.gene.behavior

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.genetics_resequenced.entity.goals.FrenzyMeleeAttackGoal
import dev.aaronhowser.mods.genetics_resequenced.entity.goals.FrenzyTargetGoal
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySelector
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.bee.Bee
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent
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

	fun inheritGenes(event: BabyEntitySpawnEvent) {
		val parentA = event.parentA
		val parentB = event.parentB

		val child = event.child ?: return

		val aGenes = parentA.permanentGeneHolders
		val bGenes = parentB.permanentGeneHolders

		if (aGenes.isEmpty() && bGenes.isEmpty()) return

		val commonGenes = aGenes.intersect(bGenes)
		val uniqueGenes = aGenes.union(bGenes) - commonGenes

		for (gene in commonGenes) {
			child.addGene(gene)
		}

		for (gene in uniqueGenes) {
			if (parentA.random.nextBoolean()) {
				child.addGene(gene)
			}
		}
	}

	@JvmStatic
	fun shouldPlacidCancelTargetGoal(goal: TargetGoal): Boolean {
		return goal.mob.hasGene(ModGenes.PLACID)
	}

	// https://github.com/Elenterius/Biomancy/blob/mc1.20.1/prod/src/main/java/com/github/elenterius/biomancy/serum/FrenzySerum.java#L99
	fun giveFrenzyGoals(mob: Mob) {
		if (mob !is PathfinderMob) return

		val frenzyGene = ModGenes.FRENZIED.getHolderOrThrow(mob.registryAccess())
		if (frenzyGene.isDisabled) return

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
