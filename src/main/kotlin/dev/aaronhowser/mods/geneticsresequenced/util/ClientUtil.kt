package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.aaron.AaronExtensions.isTrue
import dev.aaronhowser.mods.aaron.AaronExtensions.status
import dev.aaronhowser.mods.aaron.ServerScheduler
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.ClickGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.core.RegistryAccess
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.world.entity.player.PlayerModelPart

object ClientUtil {

	val localRegistryAccess: RegistryAccess?
		get() = Minecraft.getInstance().level?.registryAccess()

	fun playerIsCreative(): Boolean = AaronClientUtil.localPlayer?.isCreative.isTrue()

	private val options: Options
		get() = Minecraft.getInstance().options

	private var removedSkinLayers: Set<PlayerModelPart> = emptySet()
	fun shearPlayerSkin() {
		val enabledModelParts = this.options.modelParts.toMutableSet()
		if (!ClientConfig.woolyRemovesCape.get()) {
			enabledModelParts.remove(PlayerModelPart.CAPE)
		}

		for (part in enabledModelParts) {
			this.options.toggleModelPart(part, false)
		}

		this.removedSkinLayers = enabledModelParts

		GeneticsResequenced.LOGGER.info("Sheared layers off player skin: ${this.removedSkinLayers.joinToString(", ")}")

		val addLayersBackTask = { addSkinLayersBack() }

		ClickGenes.RECENTLY_SHEARED_ENTITIES.cooldownEndedTasks.add(addLayersBackTask)
	}

	fun addSkinLayersBack() {
		if (this.removedSkinLayers.isEmpty()) return
		for (part in this.removedSkinLayers) {
			this.options.toggleModelPart(part, true)
		}

		GeneticsResequenced.LOGGER.info("Added layers back to player skin: ${this.removedSkinLayers.joinToString(", ")}")
		this.removedSkinLayers = emptySet()
	}

	private var amountTryingToChangeLanguage = 0
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

		if (ClientConfig.disableCringeLangChange.get()) {
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

		fun sendSystemMessage(message: Component) {
			AaronClientUtil.localPlayer?.sendSystemMessage(message)
		}

		ServerScheduler.scheduleTaskInTicks(1) {
			val component = if (wasAdded) {
				ModMessageLang.CRINGE_ADDED.toComponent(countdownSeconds)
			} else {
				ModMessageLang.CRINGE_REMOVED.toComponent(countdownSeconds)
			}.withStyle {
				it.withHoverEvent(
					HoverEvent(
						HoverEvent.Action.SHOW_TEXT,
						ModMessageLang.CRINGE_CONFIG.toComponent()
					)
				)
			}

			sendSystemMessage(component)
		}

		var secondsLeft = countdownSeconds

		while (secondsLeft > 0) {
			val scheduleIn = 20 * (countdownSeconds - secondsLeft)
			if (scheduleIn != 0) {
				val secondsLeftFinal = secondsLeft

				ServerScheduler.scheduleTaskInTicks(scheduleIn) {
					AaronClientUtil.localPlayer?.status(Component.literal("$secondsLeftFinal..."))
				}
			}

			secondsLeft--
		}

		this.amountTryingToChangeLanguage++
		ServerScheduler.scheduleTaskInTicks(20 * countdownSeconds) {
			sendSystemMessage(
				ModMessageLang.CRINGE_RELOADING
					.toComponent()
					.withStyle {
						it.withHoverEvent(
							HoverEvent(
								HoverEvent.Action.SHOW_TEXT,
								ModMessageLang.CRINGE_CONFIG.toComponent()
							)
						)
					}
			)

			if (this.amountTryingToChangeLanguage == 1) {
				Minecraft.getInstance().reloadResourcePacks()
				this.amountTryingToChangeLanguage--
			} else {
				GeneticsResequenced.LOGGER.warn("Tried to reload resources, but it would have caused a concurrency error!")
			}
		}

	}

}