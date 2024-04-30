package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.ClickGenes.recentlySheered
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.PlayerModelPart

object ClientHelper {

    fun playerIsCreative(): Boolean = Minecraft.getInstance().player?.isCreative == true
    fun playerIsSpectator(): Boolean = Minecraft.getInstance().player?.isSpectator == true

    fun playerSkinSheared() {
        val options = Minecraft.getInstance().options
        try {
            val modelPartsField = options::class.java.getDeclaredField("modelParts")
            modelPartsField.isAccessible = true

            @Suppress("UNCHECKED_CAST")
            val modelParts = (modelPartsField.get(options) as MutableSet<PlayerModelPart>).toSet()

            for (part in modelParts) {
                options.toggleModelPart(part, false)
            }

            GeneticsResequenced.LOGGER.info("Sheared ${modelParts.size} layers off player skin")

            val addLayersBackTask = {
                for (part in modelParts) {
                    options.toggleModelPart(part, true)
                }

                GeneticsResequenced.LOGGER.info("Added ${modelParts.size} layers back to player skin")
            }

            recentlySheered.cooldownEndedTasks.add(addLayersBackTask)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}