package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.aaron.AaronExtensions.isEntity
import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.item.AntiFieldOrbItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.*
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.monster.piglin.Piglin
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import kotlin.math.max

object TickGenes {

	fun handleBioluminescence(entity: LivingEntity) {
		if (!entity.hasGene(ModGenes.BIOLUMINESCENCE)) return

		if (entity.tickCount % ServerConfig.CONFIG.bioluminescenceCooldown.get() != 0) return

		val level = entity.level()
		if (level.getBrightness(LightLayer.BLOCK, entity.blockPosition()) > 8) return

		val headBlock = level.getBlockState(entity.blockPosition().above())
		if (!headBlock.isAir) return

		level.setBlockAndUpdate(
			entity.blockPosition().above(),
			ModBlocks.BIOLUMINESCENCE_BLOCK.get().defaultBlockState()
		)
	}

	fun handlePhotosynthesis(entity: LivingEntity) {
		if (entity !is Player) return
		if (entity.tickCount % ServerConfig.CONFIG.photosynthesisCooldown.get() != 0) return

		if (!entity.hasGene(ModGenes.PHOTOSYNTHESIS)) return

		val foodData = entity.foodData
		if (!foodData.needsFood()) return

		val inDirectSunlight = entity.level().canSeeSky(entity.blockPosition())
		val isDay = entity.level().isDay
		if (!inDirectSunlight || !isDay) return

		foodData.eat(
			ServerConfig.CONFIG.photosynthesisHungerAmount.get(),
			ServerConfig.CONFIG.photosynthesisSaturationAmount.get().toFloat()
		)
	}

	fun handleNoHunger(entity: Player) {
		if (entity.tickCount % ServerConfig.CONFIG.noHungerCooldown.get() != 0) return
		if (!entity.hasGene(ModGenes.NO_HUNGER)) return

		val foodData = entity.foodData
		foodData.foodLevel = max(foodData.foodLevel, ServerConfig.CONFIG.noHungerMinimum.get())
	}

	private fun isDeathGene(geneHolder: Holder<Gene>): Boolean {
		return geneHolder.isGene(ModGenes.BLACK_DEATH)
				|| geneHolder.isGene(ModGenes.GREEN_DEATH)
				|| geneHolder.isGene(ModGenes.GRAY_DEATH)
				|| geneHolder.isGene(ModGenes.UN_UNDEATH)
				|| geneHolder.isGene(ModGenes.WHITE_DEATH)
	}

	fun handleTickingGenes(entity: LivingEntity) {
		if (entity.tickCount % ServerConfig.CONFIG.passivesCheckCooldown.get() != 0) return
		if (entity !is Mob && entity !is Player) return

		val geneHolders = entity.geneHolders

		val potionGenes = mutableListOf<Holder<Gene>>()

		for (geneHolder in geneHolders) {
			if (geneHolder.isDisabled) continue

			if (geneHolder.value().potions.isNotEmpty()) potionGenes.add(geneHolder)

			when {
				geneHolder.isGene(ModGenes.WATER_BREATHING) -> entity.airSupply = entity.maxAirSupply
				geneHolder.isGene(ModGenes.FLAMBE) -> entity.remainingFireTicks = ServerConfig.CONFIG.passivesCheckCooldown.get() * 2 * 20
				geneHolder.isGene(ModGenes.LAY_EGG) -> handleLayEgg(entity)
				geneHolder.isGene(ModGenes.MEATY_TWO) -> handleMeatyTwo(entity)

				isDeathGene(geneHolder) -> handleDeathGenes(entity, geneHolder)
			}
		}

		handlePotionGenes(entity, potionGenes)
	}

	private fun handleDeathGenes(entity: LivingEntity, geneHolder: Holder<Gene>) {
		if (geneHolder.isGene(ModGenes.BLACK_DEATH)) {
			entity.hurt(virusDamageSource(entity.level()), entity.maxHealth * 1000)
			entity.kill()

			// I have no idea if this is even necessary
			if (entity.isAlive) {
				val damageSources = entity.level().damageSources()
				entity.hurt(damageSources.fellOutOfWorld(), entity.maxHealth * 1000)
				entity.hurt(damageSources.magic(), entity.maxHealth * 1000)
				entity.hurt(damageSources.wither(), entity.maxHealth * 1000)

				if (entity.isAlive) {
					entity.remove(Entity.RemovalReason.KILLED)
				}
			}
		}

		val entityPredicate: (LivingEntity) -> Boolean = when {
			geneHolder.isGene(ModGenes.GREEN_DEATH) -> { it -> it is Creeper }
			geneHolder.isGene(ModGenes.UN_UNDEATH) -> { it -> it.isEntity(EntityTypeTags.UNDEAD) }
			geneHolder.isGene(ModGenes.GRAY_DEATH) -> { it -> it is AgeableMob || it is Zombie || it is Piglin }
			geneHolder.isGene(ModGenes.WHITE_DEATH) -> { it -> it.type.category == MobCategory.MONSTER }
			else -> return
		}

		if (!entityPredicate(entity)) return
		entity.hurt(virusDamageSource(entity.level()), maxOf(entity.health / 2, 2f))
	}

	private fun virusDamageSource(level: Level): DamageSource = level.damageSources().source(VIRUS_DAMAGE_KEY)
	private val VIRUS_DAMAGE_KEY: ResourceKey<DamageType> =
		ResourceKey.create(Registries.DAMAGE_TYPE, OtherUtil.modResource("virus"))

	private val GENE_INFERIORITY_MAP: Map<ResourceKey<Gene>, List<ResourceKey<Gene>>> = mapOf(
		ModGenes.SPEED_FOUR to listOf(ModGenes.SPEED, ModGenes.SPEED_TWO),
		ModGenes.SPEED_TWO to listOf(ModGenes.SPEED),
		ModGenes.REGENERATION_FOUR to listOf(ModGenes.REGENERATION),
		ModGenes.HASTE_TWO to listOf(ModGenes.HASTE),
		ModGenes.RESISTANCE_TWO to listOf(ModGenes.RESISTANCE),
		ModGenes.STRENGTH_TWO to listOf(ModGenes.STRENGTH),
		ModGenes.POISON_FOUR to listOf(ModGenes.POISON),
		ModGenes.SLOWNESS_FOUR to listOf(ModGenes.SLOWNESS),
		ModGenes.SLOWNESS_SIX to listOf(ModGenes.SLOWNESS, ModGenes.SLOWNESS_FOUR)
	)

	private fun handlePotionGenes(entity: LivingEntity, genesWithPotions: MutableList<Holder<Gene>>) {
		if (genesWithPotions.isEmpty()) return

		val genesToSkip = mutableListOf<ResourceKey<Gene>>()

		for (geneHolder in genesWithPotions.toList()) {
			GENE_INFERIORITY_MAP[geneHolder.key]?.let { redundantGenes ->
				genesToSkip.addAll(redundantGenes)
			}
		}

		genesWithPotions.removeAll(genesToSkip.map { it.getHolderOrThrow(entity.registryAccess()) })

		for (geneHolder in genesWithPotions) {
			for (genePotion in geneHolder.value().potions) {
				val existingEffect = entity.getEffect(genePotion.effect)
				if (existingEffect != null && existingEffect.amplifier >= genePotion.amplifier) continue

				entity.removeEffect(genePotion.effect)
				entity.addEffect(genePotion)
			}
		}
	}

	fun handlePotionGeneRemoved(entity: LivingEntity, removedGene: Holder<Gene>) {
		val genePotions = removedGene.value().potions
		for (genePotion in genePotions) {
			val existingEffect = entity.getEffect(genePotion.effect) ?: continue
			if (existingEffect.amplifier != genePotion.amplifier) continue

			entity.removeEffect(genePotion.effect)
		}
	}

	private val RECENTLY_MEATED_TWO = GeneCooldown(
		ModGenes.MEATY_TWO,
		ServerConfig.CONFIG.meaty2Cooldown.get(),
		notifyPlayer = false
	)

	private fun handleMeatyTwo(entity: LivingEntity) {
		val newlyMeated = RECENTLY_MEATED_TWO.add(entity)
		if (!newlyMeated) return

		val luck = entity.activeEffects.find { it.effect == MobEffects.LUCK }?.amplifier ?: 0

		val meatEntity = ItemEntity(
			entity.level(),
			entity.x,
			entity.y,
			entity.z,
			ItemStack(Items.COOKED_PORKCHOP, 1 + luck)
		)

		entity.level().addFreshEntity(meatEntity)
	}

	private val RECENTLY_LAID_EGGS = GeneCooldown(
		ModGenes.LAY_EGG,
		ServerConfig.CONFIG.eggCooldown.get(),
		notifyPlayer = false
	)

	private fun handleLayEgg(entity: LivingEntity) {
		val hasNotRecentlyLainEgg = RECENTLY_LAID_EGGS.add(entity)
		if (!hasNotRecentlyLainEgg) return

		val luck = entity.activeEffects.find { it.effect == MobEffects.LUCK }?.amplifier ?: 0

		val eggEntity = ItemEntity(
			entity.level(),
			entity.x,
			entity.y,
			entity.z,
			ItemStack(Items.EGG, 1 + luck)
		)

		entity.level().addFreshEntity(eggEntity)
	}

	fun handleItemMagnet(player: Player) {
		if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return
		if (player.tickCount % ServerConfig.CONFIG.itemMagnetCooldown.get() != 0) return
		if (!player.hasGene(ModGenes.ITEM_MAGNET)) return
		if (AntiFieldOrbItem.isActiveForPlayer(player)) return

		val nearbyItems = player.level().getEntitiesOfClass(
			ItemEntity::class.java,
			player.boundingBox.inflate(ServerConfig.CONFIG.itemMagnetRadius.get())
		)

		for (itemEntity in nearbyItems) {
			if (itemEntity.item.count <= 0) continue
			if (itemEntity.owner == player && itemEntity.age < 20 * 3) continue
			if (itemEntity.item.isItem(ModItemTagsProvider.MAGNET_ITEM_BLACKLIST)) continue

			if (AntiFieldBlock.isNearActiveAntifield(player.level(), itemEntity.blockPosition())) continue

			itemEntity.playerTouch(player)
		}
	}

	fun itemMagnetBlacklistTooltip(event: ItemTooltipEvent) {
		if (!ClientConfig.CONFIG.itemMagnetBlacklistTooltip.get()) return

		val player = event.entity ?: return
		if (!player.hasGene(ModGenes.ITEM_MAGNET)) return

		val item = event.itemStack
		if (!item.isItem(ModItemTagsProvider.MAGNET_ITEM_BLACKLIST)) return

		val component = ModTooltipLang.ITEM_MAGNET_BLACKLIST
			.toComponent()
			.withStyle(ChatFormatting.DARK_GRAY)
		event.toolTip.add(component)
	}

	fun handleXpMagnet(player: Player) {
		if (player.isCrouching || player.isDeadOrDying || player.isSpectator) return
		if (player.tickCount % ServerConfig.CONFIG.xpMagnetCooldown.get() != 0) return
		if (!player.hasGene(ModGenes.XP_MAGNET)) return
		if (AntiFieldOrbItem.isActiveForPlayer(player)) return

		val nearbyXpOrbs = player.level().getEntitiesOfClass(
			ExperienceOrb::class.java,
			player.boundingBox.inflate(ServerConfig.CONFIG.xpMagnetRadius.get())
		)

		for (xpOrb in nearbyXpOrbs) {
			if (AntiFieldBlock.isNearActiveAntifield(player.level(), xpOrb.blockPosition())) continue

			xpOrb.playerTouch(player)
			player.takeXpDelay = 1
		}
	}


}