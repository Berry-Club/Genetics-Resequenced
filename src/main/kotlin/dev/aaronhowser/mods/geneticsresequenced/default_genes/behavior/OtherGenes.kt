package dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.NarratorPacket
import net.minecraft.network.chat.Component
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

    fun handleNarrator(event: ServerChatEvent.Submitted) {
        val player = event.player
        val genes = player.getGenes() ?: return

        if (!genes.hasGene(DefaultGenes.chatterbox)) return

        val message = event.message

        ModPacketHandler.messageNearbyPlayers(
            NarratorPacket(message.string),
            player.level as ServerLevel,
            player.position(),
            64.0
        )
    }

    fun handleCringe(event: ServerChatEvent.Submitted) {

        val player = event.player
        val genes = player.getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.cringe)) return

        if (event.message.string.startsWith("/")) return

        val substitutions = mapOf(
            "r" to "w",
            "l" to "w",
            "R" to "W",
            "L" to "W",
            "ove" to "uv",
            "th" to "d",
            "Th" to "D"
        )

        var uwuified = event.message.string
        for ((from, to) in substitutions) {
            uwuified = uwuified.replace(from, to)
        }

        val interjections = listOf(
            " >w<",
            " UwU",
            " owo",
            " ^w^",
            " >.<",
            " (✿◠‿◠)",
            " (´・ω・`)",
            " :3",
            " (◕‿◕✿)",
        )
        val words = uwuified.split(" ").toMutableList()

        for (i in 1 until words.size step 4) {
            words[i] += interjections.random()
        }

        val uwuifiedMessage = words.joinToString(" ")

        event.message = Component.literal(uwuifiedMessage)
    }

}