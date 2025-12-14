package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.aaron.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.item.DragonHealthCrystal
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEffects
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent

object DamageGenes {

	// Canceling

	fun handleNoFallDamage(event: EntityInvulnerabilityCheckEvent) {
		if (!event.source.`is`(DamageTypes.FALL)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.NO_FALL_DAMAGE)) {
			event.isInvulnerable = true
		}
	}

	fun handleWitherProof(event: EntityInvulnerabilityCheckEvent) {
		if (!event.source.`is`(DamageTypes.WITHER)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.WITHER_PROOF)) {
			entity.removeEffect(MobEffects.WITHER)
			event.isInvulnerable = true
		}
	}

	fun handleFireProof(event: EntityInvulnerabilityCheckEvent) {
		if (!event.source.`is`(DamageTypes.IN_FIRE) && !event.source.`is`(DamageTypes.ON_FIRE)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.FIRE_PROOF)) {
			entity.clearFire()
			event.isInvulnerable = true
		}
	}

	fun handleLavaProof(event: EntityInvulnerabilityCheckEvent) {
		if (!event.source.`is`(DamageTypes.LAVA)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.LAVA_PROOF)) {
			event.isInvulnerable = true
		}
	}

	fun handlePoisonProof(event: EntityInvulnerabilityCheckEvent) {
		if (!event.source.`is`(NeoForgeMod.POISON_DAMAGE)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.POISON_IMMUNITY)) {
			entity.removeEffect(MobEffects.POISON)
			event.isInvulnerable = true
		}
	}

	// Changing amount (not just canceling)

	fun handleDragonHealth(event: LivingDamageEvent.Pre) {
		DragonHealthCrystal.handleIncomingDamage(event)
	}

	fun handleJohnny(event: LivingDamageEvent.Pre) {
		val attacker = event.container.source.entity as? LivingEntity ?: return
		if (!attacker.hasGene(ModGenes.JOHNNY)) return

		val weaponIsAxe = attacker.mainHandItem.item.canPerformAction(attacker.mainHandItem, ItemAbilities.AXE_DIG)
		if (!weaponIsAxe) return

		event.container.newDamage *= ServerConfig.CONFIG.johnnyAttackMultiplier.get().toFloat()
	}

	fun handleClawsDamageBonus(event: LivingDamageEvent.Pre) {
		val attacker = event.container.source.entity as? LivingEntity ?: return
		if (attacker.mainHandItem.isNotEmpty()) return

		val clawsLevel = when {
			attacker.hasGene(ModGenes.CLAWS_TWO) -> 2
			attacker.hasGene(ModGenes.CLAWS) -> 1
			else -> return
		}

		val additionalDamage = ServerConfig.CONFIG.clawsDamage.get() * clawsLevel
		event.container.newDamage += additionalDamage.toFloat()
	}

	// Triggers

	fun handleWebDefense(event: LivingDamageEvent.Post) {
		val victim = event.entity
		if (!victim.hasGene(ModGenes.WEB_DEFENSE)) return

		val attacker = event.source.entity as? LivingEntity ?: return
		val level = attacker.level()

		if (!level.random.chance(ServerConfig.CONFIG.webDefenseChance.get())) return

		val webPos = attacker.blockPosition()
		if (level.getBlockState(webPos).canBeReplaced()) {
			level.setBlockAndUpdate(webPos, ModBlocks.WEB_DEFENSE_BLOCK.get().defaultBlockState())
		}
	}

	fun handleWitherHit(event: LivingDamageEvent.Post) {
		// Makes it not proc if it's an arrow or whatever
		if (!event.source.isDirect) return

		val victim = event.entity
		val attacker = event.source.entity as? LivingEntity ?: return

		if (attacker == victim) return

		if (!attacker.hasGene(ModGenes.WITHER_HIT)) return

		val witherEffect = MobEffectInstance(
			MobEffects.WITHER,
			100
		)

		victim.addEffect(witherEffect)
	}

	fun handleThorns(event: LivingDamageEvent.Post) {
		val attacker = event.source.entity as? LivingEntity ?: return

		val target = event.entity as? Mob ?: event.entity as? Player ?: return
		if (target == attacker) return

		val chestPlate = target.getItemBySlot(EquipmentSlot.CHEST)
		val targetChestplateMissingOrLeather = chestPlate.isEmpty || chestPlate.`is`(Items.LEATHER_CHESTPLATE)
		if (!targetChestplateMissingOrLeather) return

		if (!target.hasGene(ModGenes.THORNS)) return

		if (!target.random.chance(ServerConfig.CONFIG.thornsChance.get())) return

		val thornsDamageSource = target.level().damageSources().thorns(target)
		attacker.hurt(thornsDamageSource, ServerConfig.CONFIG.thornsDamage.get().toFloat())

		if (target is Player) {
			target.causeFoodExhaustion(ServerConfig.CONFIG.thornsHungerDrain.get().toFloat())
		}
	}

	fun handleClawsBleeding(event: LivingDamageEvent.Post) {
		val attacker = event.source.entity as? LivingEntity ?: return
		if (attacker.mainHandItem.isNotEmpty()) return

		val clawsLevel = when {
			attacker.hasGene(ModGenes.CLAWS_TWO) -> 2
			attacker.hasGene(ModGenes.CLAWS) -> 1
			else -> return
		}

		val chanceOfHappening = ServerConfig.CONFIG.clawsChance.get() * clawsLevel
		if (!attacker.random.chance(chanceOfHappening)) return

		event.entity.addEffect(
			MobEffectInstance(
				ModEffects.BLEED,
				20 * 5,
				0,
				false,
				true,
				true
			),
			attacker
		)
	}

	fun handleChilling(event: LivingDamageEvent.Post) {
		if (!event.source.isDirect) return

		val attacker = event.source.entity as? LivingEntity ?: return
		if (!attacker.hasGene(ModGenes.CHILLING)) return

		if (!attacker.random.chance(ServerConfig.CONFIG.chillChance.get())) return

		val target = event.entity
		target.ticksFrozen = ServerConfig.CONFIG.chillDuration.get()
	}

}