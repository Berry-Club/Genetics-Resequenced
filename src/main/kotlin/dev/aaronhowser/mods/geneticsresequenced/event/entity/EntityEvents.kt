package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GeneCooldowns
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.getActiveGenes
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.geneticsresequenced.attachment.TemporaryGenesData
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DamageGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DeathGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.MobGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.living.*
import net.neoforged.neoforge.event.level.ExplosionEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent

@EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID
)
object EntityEvents {

	@SubscribeEvent
	fun onLivingDeath(event: LivingDeathEvent) {
		DeathGenes.handleEmeraldHeart(event)
		DeathGenes.handleExplosiveExit(event)
		DeathGenes.handleSlimyDeath(event)
	}

	@SubscribeEvent
	fun onDetonate(event: ExplosionEvent.Detonate) {
		DeathGenes.explosiveExitDetonation(event)
	}

	@SubscribeEvent
	fun onEntityInvulnerabilityCheck(event: EntityInvulnerabilityCheckEvent) {
		DamageGenes.handleNoFallDamage(event)
		DamageGenes.handleWitherProof(event)
		DamageGenes.handleFireProof(event)
		DamageGenes.handleLavaProof(event)
		DamageGenes.handlePoisonProof(event)
	}

	@SubscribeEvent
	fun onLivingDamagePre(event: LivingDamageEvent.Pre) {
		DamageGenes.handleDragonHealth(event)
		DamageGenes.handleJohnny(event)
		DamageGenes.handleClawsDamageBonus(event)
	}

	@SubscribeEvent
	fun onLivingHurtPost(event: LivingDamageEvent.Post) {
		if (event.newDamage <= 0f) return

		DamageGenes.handleThorns(event)
		DamageGenes.handleClawsBleeding(event)
		DamageGenes.handleWitherHit(event)
		DamageGenes.handleChilling(event)
		DamageGenes.handleWebDefense(event)
	}

	@SubscribeEvent
	fun onEntityTick(event: EntityTickEvent.Pre) {
		val entity = event.entity as? LivingEntity ?: return

		val genes = entity.getActiveGenes()

		TickGenes.handleMiscGenes(entity, genes)
		GeneCooldowns.tick(entity, genes)
	}

	@SubscribeEvent
	fun afterEntityTick(event: EntityTickEvent.Post) {
		val entity = event.entity as? LivingEntity ?: return

		TemporaryGenesData.tickTemporaryGenes(entity)
	}

	@SubscribeEvent
	fun onEntitySpawn(event: EntityJoinLevelEvent) {
		val entity = event.entity
		if (entity is PathfinderMob) {
			MobGenes.attachScareGoals(entity)
			MobGenes.giveFrenzyGoals(entity)
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onBabySpawn(event: BabyEntitySpawnEvent) {
		if (event.isCanceled) return

		inheritGenes(event)
		MobGenes.handleFertile(event)
	}

	private fun inheritGenes(event: BabyEntitySpawnEvent) {
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

	@SubscribeEvent
	fun onMobDespawn(event: MobDespawnEvent) {
		val mob = event.entity
		if (mob.permanentGeneHolders.isNotEmpty()) {
			event.result = MobDespawnEvent.Result.DENY
		}
	}

	@SubscribeEvent
	fun onLivingDropExperience(event: LivingExperienceDropEvent) {
		DeathGenes.handleExperienced(event)
	}

}