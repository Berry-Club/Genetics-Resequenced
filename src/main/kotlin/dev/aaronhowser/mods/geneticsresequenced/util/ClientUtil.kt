package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.configs.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.ClickGenes.recentlySheered
import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.resources.language.LanguageInfo
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.world.entity.player.PlayerModelPart

object ClientUtil {

    val localPlayer: LocalPlayer?
        get() = Minecraft.getInstance().player

    fun playerIsCreative(): Boolean = localPlayer?.isCreative == true
    fun playerIsSpectator(): Boolean = localPlayer?.isSpectator == true

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

        recentlySheered.cooldownEndedTasks.add(addLayersBackTask)
    }

    fun addSkinLayersBack() {
        if (removedSkinLayers.isEmpty()) return
        for (part in removedSkinLayers) {
            options.toggleModelPart(part, true)
        }

        GeneticsResequenced.LOGGER.info("Added layers back to player skin: ${removedSkinLayers.joinToString(", ")}")
        removedSkinLayers = emptySet()
    }

    private var canReloadResources = false
    private var nonCringeLanguage: LanguageInfo? = null
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

        val lolcat = languageManager.getLanguage("lol_us")

        if (wasAdded) {
            if (!currentLanguage.code.startsWith("en_")) {
                GeneticsResequenced.LOGGER.warn("Cringe language-changing is only available in English!")
                return
            }

            nonCringeLanguage = currentLanguage
            languageManager.selected = lolcat

            canReloadResources = true
            GeneticsResequenced.LOGGER.info("Changed language to cringe!")
        } else {
            if (languageManager.selected != lolcat) return

            if (nonCringeLanguage == null && languageManager.selected == lolcat) {
                GeneticsResequenced.LOGGER.warn("Tried to remove cringe language, but no non-cringe language was saved!")
                return
            }

            languageManager.selected = nonCringeLanguage ?: languageManager.getLanguage("en_us")
            nonCringeLanguage = null

            canReloadResources = false
            GeneticsResequenced.LOGGER.info("Changed language back to non-cringe!")
        }

        fun sendSystemMessage(message: Component) {
            localPlayer?.sendSystemMessage(message)
        }

        ModScheduler.scheduleTaskInTicks(1) {
            sendSystemMessage(
                Component.translatable(
                    if (wasAdded) "message.geneticsresequenced.cringe.resources.add" else "message.geneticsresequenced.cringe.resources.remove",
                    countdownSeconds
                ).withStyle {
                    it.withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            Component.translatable(
                                "message.geneticsresequenced.cringe.resources.tooltip"
                            )
                        )
                    )
                }
            )
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

        ModScheduler.scheduleTaskInTicks(20 * countdownSeconds) {
            sendSystemMessage(
                Component
                    .translatable("message.geneticsresequenced.cringe.resources.reloading")
                    .withStyle {
                        it.withHoverEvent(
                            HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Component.translatable(
                                    "message.geneticsresequenced.cringe.resources.tooltip"
                                )
                            )
                        )
                    }
            )

            if (canReloadResources) {
                Minecraft.getInstance().reloadResourcePacks()
                canReloadResources = false
            }
        }

    }

}