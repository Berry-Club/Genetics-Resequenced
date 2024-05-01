package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.ClickGenes.recentlySheered
import net.minecraft.client.Minecraft
import net.minecraft.client.Options
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.PlayerModelPart
import net.minecraftforge.fml.util.ObfuscationReflectionHelper

object ClientHelper {

    private val localPlayer: LocalPlayer? = Minecraft.getInstance().player

    fun playerIsCreative(): Boolean = localPlayer?.isCreative == true
    fun playerIsSpectator(): Boolean = localPlayer?.isSpectator == true

    private val options: Options = Minecraft.getInstance().options

    private var removedSkinLayers: Set<PlayerModelPart> = emptySet()

    fun shearPlayerSkin() {
        try {
            val enabledModelParts = options.modelParts
            for (part in enabledModelParts) {
                options.toggleModelPart(part, false)
            }

            removedSkinLayers = enabledModelParts

            GeneticsResequenced.LOGGER.info("Sheared layers off player skin: ${removedSkinLayers.joinToString(", ")}")

            val addLayersBackTask = { addSkinLayersBack() }

            recentlySheered.cooldownEndedTasks.add(addLayersBackTask)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addSkinLayersBack() {
        for (part in removedSkinLayers) {
            options.toggleModelPart(part, true)
        }

        GeneticsResequenced.LOGGER.info("Added layers back to player skin: ${removedSkinLayers.joinToString(", ")}")
        removedSkinLayers = emptySet()
    }

}