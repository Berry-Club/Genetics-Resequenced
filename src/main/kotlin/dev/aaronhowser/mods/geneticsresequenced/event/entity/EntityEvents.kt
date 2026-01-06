package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.permanentGeneHolders
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.capability.TemporaryGenesCapability
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DamageGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DeathGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.MobGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.EntityJoinLevelEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.level.ExplosionEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID
)
object EntityEvents {

	@SubscribeEvent
	fun <T> onAttachCapabilities(event: AttachCapabilitiesEvent<T>) {
		val obj = event.getObject()

		if (obj is LivingEntity) {
			val geneProvider = GenesCapabilityProvider(obj.level().registryAccess())
			event.addCapability(GenesCapabilityProvider.CAPABILITY_RL, geneProvider)
		}
	}

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
	fun onLivingAttack(event: LivingAttackEvent) {
		DamageGenes.handleNoFallDamage(event)
		DamageGenes.handleWitherProof(event)
		DamageGenes.handleFireProof(event)
		DamageGenes.handleLavaProof(event)
		DamageGenes.handlePoisonProof(event)
	}

	@SubscribeEvent
	fun onLivingDamage(event: LivingDamageEvent) {
		DamageGenes.handleDragonHealth(event)
		DamageGenes.handleJohnny(event)
		DamageGenes.handleClawsDamageBonus(event)
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	fun onLivingHurtPost(event: LivingDamageEvent) {
		if (event.amount <= 0f || event.isCanceled) return

		DamageGenes.handleThorns(event)
		DamageGenes.handleClawsBleeding(event)
		DamageGenes.handleWitherHit(event)
		DamageGenes.handleChilling(event)
		DamageGenes.handleWebDefense(event)
	}

	@SubscribeEvent
	fun onEntityTick(event: LivingEvent.LivingTickEvent) {
		val entity = event.entity

		TickGenes.handleBioluminescence(entity)
		TickGenes.handlePhotosynthesis(entity)
		TickGenes.handleTickingGenes(entity)

		TemporaryGenesCapability.tickTemporaryGenes(entity)
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
	fun onLivingDropExperience(event: LivingExperienceDropEvent) {
		DeathGenes.handleExperienced(event)
	}

}