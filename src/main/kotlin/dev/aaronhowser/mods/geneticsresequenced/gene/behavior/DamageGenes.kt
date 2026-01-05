package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.aaron.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.AaronExtensions.isDamageSource
import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
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
import net.minecraftforge.common.ToolActions
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDamageEvent

object DamageGenes {

	// Canceling

	fun handleNoFallDamage(event: LivingAttackEvent) {
		if (!event.source.`is`(DamageTypes.FALL)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.NO_FALL_DAMAGE)) {
			event.isCanceled = true
		}
	}

	fun handleWitherProof(event: LivingAttackEvent) {
		if (!event.source.isDamageSource(DamageTypes.WITHER)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.WITHER_PROOF)) {
			entity.removeEffect(MobEffects.WITHER)
			event.isCanceled = true
		}
	}

	fun handleFireProof(event: LivingAttackEvent) {
		if (!event.source.isDamageSource(DamageTypes.IN_FIRE) && !event.source.isDamageSource(DamageTypes.ON_FIRE)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.FIRE_PROOF)) {
			entity.clearFire()
			event.isCanceled = true
		}
	}

	fun handleLavaProof(event: LivingAttackEvent) {
		if (!event.source.isDamageSource(DamageTypes.LAVA)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.LAVA_PROOF)) {
			event.isCanceled = true
		}
	}

	fun handlePoisonProof(event: LivingAttackEvent) {
		if (!event.source.isDamageSource(DamageTypes.MAGIC)) return

		val entity = event.entity
		if (entity.hasGene(ModGenes.POISON_IMMUNITY)) {
			entity.removeEffect(MobEffects.POISON)
			event.isCanceled = true
		}
	}

	// Changing amount (not just canceling)

	fun handleDragonHealth(event: LivingDamageEvent) {
		DragonHealthCrystal.handleIncomingDamage(event)
	}

	fun handleJohnny(event: LivingDamageEvent) {
		val attacker = event.source.entity as? LivingEntity ?: return
		if (!attacker.hasGene(ModGenes.JOHNNY)) return

		val weaponIsAxe = attacker.mainHandItem.item.canPerformAction(attacker.mainHandItem, ToolActions.AXE_DIG)
		if (!weaponIsAxe) return

		event.amount *= ServerConfig.CONFIG.johnnyAttackMultiplier.get().toFloat()
	}

	fun handleClawsDamageBonus(event: LivingDamageEvent) {
		val attacker = event.source.entity as? LivingEntity ?: return
		if (attacker.mainHandItem.isNotEmpty()) return

		val clawsLevel = when {
			attacker.hasGene(ModGenes.CLAWS_TWO) -> 2
			attacker.hasGene(ModGenes.CLAWS) -> 1
			else -> return
		}

		val additionalDamage = ServerConfig.CONFIG.clawsDamage.get() * clawsLevel
		event.amount += additionalDamage.toFloat()
	}

	// Triggers

	fun handleWebDefense(event: LivingDamageEvent) {
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

	fun handleWitherHit(event: LivingDamageEvent) {
		// Makes it not proc if it's an arrow or whatever
		if (event.source.isIndirect) return

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

	fun handleThorns(event: LivingDamageEvent) {
		val attacker = event.source.entity as? LivingEntity ?: return

		val target = event.entity as? Mob ?: event.entity as? Player ?: return
		if (target == attacker) return

		val chestPlate = target.getItemBySlot(EquipmentSlot.CHEST)
		val targetChestplateMissingOrLeather = chestPlate.isEmpty || chestPlate.isItem(Items.LEATHER_CHESTPLATE)
		if (!targetChestplateMissingOrLeather) return

		if (!target.hasGene(ModGenes.THORNS)) return

		if (!target.random.chance(ServerConfig.CONFIG.thornsChance.get())) return

		val thornsDamageSource = target.level().damageSources().thorns(target)
		attacker.hurt(thornsDamageSource, ServerConfig.CONFIG.thornsDamage.get().toFloat())

		if (target is Player) {
			target.causeFoodExhaustion(ServerConfig.CONFIG.thornsHungerDrain.get().toFloat())
		}
	}

	fun handleClawsBleeding(event: LivingDamageEvent) {
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
				ModEffects.BLEED.get(),
				20 * 5,
				0,
				false,
				true,
				true
			),
			attacker
		)
	}

	fun handleChilling(event: LivingDamageEvent) {
		if (event.source.isIndirect) return

		val attacker = event.source.entity as? LivingEntity ?: return
		if (!attacker.hasGene(ModGenes.CHILLING)) return

		if (!attacker.random.chance(ServerConfig.CONFIG.chillChance.get())) return

		val target = event.entity
		target.ticksFrozen = ServerConfig.CONFIG.chillDuration.get()
	}

}