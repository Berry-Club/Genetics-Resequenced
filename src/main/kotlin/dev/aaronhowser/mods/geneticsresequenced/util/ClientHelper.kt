package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.ClickGenes.recentlySheered
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.PlayerModelPart

object ClientHelper {

    fun playerIsCreative(): Boolean = Minecraft.getInstance().player?.isCreative == true
    fun playerIsSpectator(): Boolean = Minecraft.getInstance().player?.isSpectator == true

    fun shearPlayerSkin() {
        val options = Minecraft.getInstance().options
        val modelParts: Set<PlayerModelPart> = PlayerModelPart.values().toSet()

        for (part in modelParts) {
            options.toggleModelPart(part, false)
        }

        GeneticsResequenced.LOGGER.info("Sheared player skin")

        val addLayersBackTask = { addSkinLayersBack() }
        recentlySheered.cooldownEndedTasks.add(addLayersBackTask)
    }

    fun addSkinLayersBack() {
        val options = Minecraft.getInstance().options
        val modelParts: Set<PlayerModelPart> = PlayerModelPart.values().toSet()

        for (part in modelParts) {
            options.toggleModelPart(part, true)
        }

        GeneticsResequenced.LOGGER.info("Added layers back to player skin")
    }

}