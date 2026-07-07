package dev.aaronhowser.mods.genetics_resequenced.util

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.status
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withHoverText
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.getActiveGenes
import dev.aaronhowser.mods.genetics_resequenced.config.ClientConfig
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.OtherGenes
import dev.aaronhowser.mods.genetics_resequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.core.RegistryAccess
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.InteractionHand
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.PlayerModelPart

object ClientUtil {

	val localRegistryAccess: RegistryAccess?
		get() = Minecraft.getInstance().level?.registryAccess()

	fun playerIsCreative(): Boolean = AaronClientUtil.localPlayer?.isCreative.isTrue()

	private fun getOptions(): Options = Minecraft.getInstance().options

	private var removedSkinLayers: Set<PlayerModelPart> = emptySet()
	fun shearPlayerSkin() {
		val options = getOptions()

		val enabledModelParts = options.modelParts.toMutableSet()
		if (!ClientConfig.CONFIG.woolyRemovesCape.get()) {
			enabledModelParts.remove(PlayerModelPart.CAPE)
		}

		for (part in enabledModelParts) {
			options.setModelPart(part, false)
		}
		options.save()

		this.removedSkinLayers = enabledModelParts

		GeneticsResequenced.LOGGER.info("Sheared layers off player skin: ${this.removedSkinLayers.joinToString(", ")}")
	}

	fun addSkinLayersBack() {
		if (this.removedSkinLayers.isEmpty()) return

		val options = getOptions()
		for (part in this.removedSkinLayers) {
			options.setModelPart(part, true)
		}
		options.save()

		GeneticsResequenced.LOGGER.info("Added layers back to player skin: ${this.removedSkinLayers.joinToString(", ")}")
		this.removedSkinLayers = emptySet()
	}

	private var languageChangeAttempts = 0
		set(value) {
			field = value.coerceAtLeast(0)
		}

	private var nonCringeLanguage: String? = null
	fun handleCringe(
		wasAdded: Boolean,
		countdownSeconds: Int = 10
	) {
		//TODO: Make sure this actually works
		val access = this.localRegistryAccess
		if (access != null) {
			val cringe = ModGenes.CRINGE.getHolderOrThrow(access)
			if (cringe.isDisabled) return
		}

		if (ClientConfig.CONFIG.disableCringeLangChange.get()) {
			GeneticsResequenced.LOGGER.info("Cringe language-changing is disabled in the config!")
			return
		}

		val languageManager = Minecraft.getInstance().languageManager
		val currentLanguage = languageManager.selected

		val lolcat = "lol_us"

		if (wasAdded) {
			if (!currentLanguage.startsWith("en_")) {
				GeneticsResequenced.LOGGER.warn("Cringe language-changing is only available in English!")
				return
			}

			this.nonCringeLanguage = currentLanguage
			languageManager.selected = lolcat

			GeneticsResequenced.LOGGER.info("Changed language to cringe!")
		} else {
			if (languageManager.selected != lolcat) return

			if (this.nonCringeLanguage == null && languageManager.selected == lolcat) {
				GeneticsResequenced.LOGGER.warn("Tried to remove cringe language, but no non-cringe language was saved!")
				return
			}

			languageManager.selected = this.nonCringeLanguage ?: "en_us"
			this.nonCringeLanguage = null

			GeneticsResequenced.LOGGER.info("Changed language back to non-cringe!")
		}

		val localPlayer = AaronClientUtil.localPlayer ?: return

		localPlayer.level().scheduleTaskInTicks(1) {
			val component = if (wasAdded) {
				ModMessageLang.CRINGE_ADDED.toComponent(countdownSeconds)
			} else {
				ModMessageLang.CRINGE_REMOVED.toComponent(countdownSeconds)
			}.withStyle(Style.EMPTY.withHoverText(ModMessageLang.CRINGE_CONFIG.toComponent()))

			localPlayer.tell(component)
		}

		var secondsLeft = countdownSeconds

		while (secondsLeft > 0) {
			val scheduleIn = 20 * (countdownSeconds - secondsLeft)
			if (scheduleIn != 0) {
				val secondsLeftFinal = secondsLeft

				localPlayer.level().scheduleTaskInTicks(scheduleIn) {
					AaronClientUtil.localPlayer?.status(Component.literal("$secondsLeftFinal..."))
				}
			}

			secondsLeft--
		}

		this.languageChangeAttempts++
		localPlayer.level().scheduleTaskInTicks(20 * countdownSeconds) {
			localPlayer.tell(
				ModMessageLang.CRINGE_RELOADING
					.toComponent()
					.withStyle(Style.EMPTY.withHoverText(ModMessageLang.CRINGE_CONFIG.toComponent()))
			)

			if (this.languageChangeAttempts == 1) {
				Minecraft.getInstance().reloadResourcePacks()
				this.languageChangeAttempts--
			} else {
				GeneticsResequenced.LOGGER.warn("Tried to reload resources, but it would have caused a concurrency error!")
			}
		}
	}

	@JvmStatic
	fun shouldHidePotionInInventory(mobEffectInstance: MobEffectInstance): Boolean {
		val localPlayer = AaronClientUtil.localPlayer ?: return false

		val playerGeneHolders = localPlayer.getActiveGenes()

		for (geneHolder in playerGeneHolders) {
			val genePotions = geneHolder.value().potionDetails

			for (potionDetail in genePotions) {
				if (potionDetail.showIcon) continue

				if (potionDetail.effect == mobEffectInstance.effect
					&& (potionDetail.level - 1) >= mobEffectInstance.amplifier
				) {
					return true
				}
			}
		}

		return false
	}

	@JvmStatic
	fun shouldMobGlow(entityToGlow: LivingEntity): Boolean {
		val level = entityToGlow.level()
		if (level.isServerSide) return false

		if (OtherGenes.shouldMobGlowFromMobSight(entityToGlow)) {
			return true
		}

		val localPlayer = AaronClientUtil.localPlayer ?: return false
		val mainHandStack = localPlayer.getItemInHand(InteractionHand.MAIN_HAND)
		val offHandStack = localPlayer.getItemInHand(InteractionHand.OFF_HAND)

		if (mainHandStack.isItem(ModItems.METAL_SYRINGE)) {
			val syringeUuid = SpecificEntityItemComponent.getEntityUuid(mainHandStack)
			if (syringeUuid != null && syringeUuid == entityToGlow.uuid) {
				return true
			}
		}

		if (offHandStack.isItem(ModItems.METAL_SYRINGE)) {
			val syringeUuid = SpecificEntityItemComponent.getEntityUuid(offHandStack)
			if (syringeUuid != null && syringeUuid == entityToGlow.uuid) {
				return true
			}
		}

		return false
	}

}
