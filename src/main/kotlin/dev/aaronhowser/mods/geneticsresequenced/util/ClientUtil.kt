package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.component
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.ClickGenes
import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.world.entity.player.PlayerModelPart

object ClientUtil {

    val localPlayer: LocalPlayer?
        get() = Minecraft.getInstance().player

    fun playerIsCreative(): Boolean = localPlayer?.isCreative ?: false

    private val options: Options = Minecraft.getInstance().options

    private var removedSkinLayers: Set<PlayerModelPart> = emptySet()
    fun shearPlayerSkin() {
        var enabledModelParts = options.modelParts.toSet()
        if (!ClientConfig.woolyRemovesCape.get()) {
            enabledModelParts = enabledModelParts.minus(PlayerModelPart.CAPE)
        }

        for (part in enabledModelParts) {
            options.toggleModelPart(part, false)
        }

        removedSkinLayers = enabledModelParts

        GeneticsResequenced.LOGGER.info("Sheared layers off player skin: ${removedSkinLayers.joinToString(", ")}")

        val addLayersBackTask = { addSkinLayersBack() }

        ClickGenes.recentlySheered.cooldownEndedTasks.add(addLayersBackTask)
    }

    fun addSkinLayersBack() {
        if (removedSkinLayers.isEmpty()) return
        for (part in removedSkinLayers) {
            options.toggleModelPart(part, true)
        }

        GeneticsResequenced.LOGGER.info("Added layers back to player skin: ${removedSkinLayers.joinToString(", ")}")
        removedSkinLayers = emptySet()
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
        if (!ModGenes.cringe.isActive) return

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

            nonCringeLanguage = currentLanguage
            languageManager.selected = lolcat

            GeneticsResequenced.LOGGER.info("Changed language to cringe!")
        } else {
            if (languageManager.selected != lolcat) return

            if (nonCringeLanguage == null && languageManager.selected == lolcat) {
                GeneticsResequenced.LOGGER.warn("Tried to remove cringe language, but no non-cringe language was saved!")
                return
            }

            languageManager.selected = nonCringeLanguage ?: "en_us"
            nonCringeLanguage = null

            GeneticsResequenced.LOGGER.info("Changed language back to non-cringe!")
        }

        fun sendSystemMessage(message: Component) {
            localPlayer?.sendSystemMessage(message)
        }

        ModScheduler.scheduleTaskInTicks(1) {

            val component = if (wasAdded) {
                ModLanguageProvider.Messages.CRINGE_ADDED.component
            } else {
                ModLanguageProvider.Messages.CRINGE_REMOVED.component
            }.withStyle {
                it.withHoverEvent(
                    HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        ModLanguageProvider.Messages.CRINGE_CONFIG.component
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

                ModScheduler.scheduleTaskInTicks(scheduleIn) {
                    localPlayer?.displayClientMessage(
                        Component.literal("$secondsLeftFinal..."),
                        true
                    )
                }
            }

            secondsLeft--
        }

        amountTryingToChangeLanguage++
        ModScheduler.scheduleTaskInTicks(20 * countdownSeconds) {
            sendSystemMessage(
                ModLanguageProvider.Messages.CRINGE_RELOADING
                    .component
                    .withStyle {
                        it.withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                ModLanguageProvider.Messages.CRINGE_CONFIG.component
                            )
                        )
                    }
            )

            if (amountTryingToChangeLanguage == 1) {
                Minecraft.getInstance().reloadResourcePacks()
                amountTryingToChangeLanguage--
            } else {
                GeneticsResequenced.LOGGER.warn("Tried to reload resources, but it would have caused a concurrency error!")
            }
        }

    }

}