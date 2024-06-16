package dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.NarratorPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraftforge.event.ServerChatEvent
import kotlin.random.Random

object OtherGenes {

    private val villagerSounds = listOf(
        SoundEvents.VILLAGER_TRADE,
        SoundEvents.VILLAGER_AMBIENT,
        SoundEvents.VILLAGER_CELEBRATE
    )

    fun handleEmeraldHeart(event: ServerChatEvent.Submitted) {
        if (Random.nextDouble() > ServerConfig.emeraldHeartChatChance.get()) return

        val player = event.player
        val genes = player.getGenes() ?: return

        if (genes.hasGene(DefaultGenes.emeraldHeart)) {
            player.level.playSound(
                null,
                player.blockPosition(),
                villagerSounds.random(),
                player.soundSource,
                1f,
                1f
            )
        }
    }

    fun sendNarrator(event: ServerChatEvent.Submitted) {
        val player = event.player
        val genes = player.getGenes() ?: return

        if (!genes.hasGene(DefaultGenes.narrator)) return

        val message = event.message

        ModPacketHandler.messageNearbyPlayers(
            NarratorPacket(message.string),
            player.level as ServerLevel,
            player.position(),
            64.0
        )

    }

}