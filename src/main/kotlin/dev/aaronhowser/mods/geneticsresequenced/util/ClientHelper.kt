package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.ClickGenes.recentlySheered
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.PlayerModelPart

object ClientHelper {

    fun playerIsCreative(): Boolean = Minecraft.getInstance().player?.isCreative == true
    fun playerIsSpectator(): Boolean = Minecraft.getInstance().player?.isSpectator == true

    private var removedSkinLayers: Set<PlayerModelPart> = emptySet()
    fun shearPlayerSkin() {
        val options = Minecraft.getInstance().options
        try {
            val modelPartsField = options::class.java.getDeclaredField("modelParts")
            modelPartsField.isAccessible = true

            @Suppress("UNCHECKED_CAST")
            removedSkinLayers = (modelPartsField.get(options) as MutableSet<PlayerModelPart>).toSet()

            for (part in removedSkinLayers) {
                options.toggleModelPart(part, false)
            }

            GeneticsResequenced.LOGGER.info("Sheared layers off player skin: ${removedSkinLayers.joinToString(", ")}")

            val addLayersBackTask = { addSkinLayersBack() }

            recentlySheered.cooldownEndedTasks.add(addLayersBackTask)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addSkinLayersBack() {
        val options = Minecraft.getInstance().options

        for (part in removedSkinLayers) {
            options.toggleModelPart(part, true)
        }

        GeneticsResequenced.LOGGER.info("Added layers back to player skin: ${removedSkinLayers.joinToString(", ")}")
        removedSkinLayers = emptySet()
    }

}