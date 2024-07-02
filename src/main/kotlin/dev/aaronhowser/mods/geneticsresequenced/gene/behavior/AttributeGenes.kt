package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.genes
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent

object AttributeGenes {

    fun handleEfficiency(event: PlayerEvent.BreakSpeed) {
        if (!ModGenes.EFFICIENCY.get().isActive) return

        val efficiencyAttribute = event.entity.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
        if (efficiencyAttribute.value <= 0.0) return

        event.newSpeed += (1 + efficiencyAttribute.value * efficiencyAttribute.value).toFloat()
    }

    fun handleWallClimbing(player: Player) {
        if (!ModGenes.WALL_CLIMBING.get().isActive) return

        if (!player.hasGene(ModGenes.WALL_CLIMBING.get())) return

        if (player.horizontalCollision || player.minorHorizontalCollision) {
            player.setDeltaMovement(
                player.deltaMovement.x,
                if (player.isCrouching) 0.0 else ServerConfig.wallClimbSpeed.get(),
                player.deltaMovement.z
            )

            player.fallDistance = 0.0f
        }
    }

    fun reGiveModifiersOnRespawn(event: PlayerRespawnEvent) {
        val player = event.entity

        for (gene in player.genes) {
            gene.setAttributeModifiers(player, true)
        }

    }

    fun respawnWithMaxHealth(event: PlayerRespawnEvent) {
        val player = event.entity

        player.health = player.maxHealth
    }

}