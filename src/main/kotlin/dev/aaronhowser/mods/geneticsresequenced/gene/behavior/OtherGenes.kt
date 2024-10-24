package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.NarratorPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.neoforged.neoforge.event.ServerChatEvent
import kotlin.random.Random

object OtherGenes {

    private val villagerSounds = listOf(
        SoundEvents.VILLAGER_TRADE,
        SoundEvents.VILLAGER_AMBIENT,
        SoundEvents.VILLAGER_CELEBRATE
    )

    fun handleEmeraldHeart(event: ServerChatEvent) {
        val emeraldHeart = ModGenes.EMERALD_HEART.getHolder(event.player.registryAccess()) ?: return
        if (emeraldHeart.isDisabled) return

        if (Random.nextDouble() > ServerConfig.emeraldHeartChatChance.get()) return

        val player = event.player

        if (player.hasGene(ModGenes.EMERALD_HEART)) {
            player.level().playSound(
                null,
                player.blockPosition(),
                villagerSounds.random(),
                player.soundSource,
                1f,
                1f
            )
        }
    }

    fun handleChatterbox(event: ServerChatEvent) {
        val chatterBox = ModGenes.CHATTERBOX.getHolder(event.player.registryAccess()) ?: return
        if (chatterBox.isDisabled) return

        val player = event.player
        if (!player.hasGene(ModGenes.CHATTERBOX)) return

        val message = event.message

        ModPacketHandler.messageNearbyPlayers(
            NarratorPacket(message.string),
            player.level() as ServerLevel,
            player.position(),
            64.0
        )
    }

    private val randomPhrases = listOf(
        "UwU",
        "owo",
        "OwO",
        "uwu",
        ">w<",
        "^w^",
        ":3",
        "^-^",
        "^_^",
        "^w^",
        ":3"
    )

    /**
     * [From Create: Estrogen](https://github.com/MayaqqDev/Estrogen/blob/architectury-1.20.1/common/src/main/java/dev/mayaqq/estrogen/utils/UwUfy.java#L18)
     */
    fun uwufyString(pInput: String): String {
        var input = pInput
        val stringLength: Int = input.length

        input = input
            .replace(Regex("[rl]"), "w")
            .replace(Regex("[RL]"), "W")
            .replace("ove", "uv")
            .replace("o", "owo")
            .replace("O", "OwO")
            .replace("!", "!!!")
            .replace("?", "???")

        if (stringLength % 3 == 0) {
            input = input.uppercase()
        }

        input = if (stringLength % 2 == 0) {
            input.replace(
                Regex("([a-zA-Z])(\\b)"),
                "$1$1$1$1$2"
            )
        } else {
            // 50% chance to duplicate the first letter and add '-'
            input.replace(
                Regex("\\b([a-zA-Z])([a-zA-Z]*)\\b"),
                "$1-$1$2"
            )
        }

        val tildes = "~".repeat(Random.nextInt(0, 4))

        return input + "$tildes " + randomPhrases.random()
    }


    fun handleCringeChat(event: ServerChatEvent) {
        val cringe = ModGenes.CRINGE.getHolder(event.player.registryAccess()) ?: return
        if (cringe.isDisabled) return

        val player = event.player
        if (!player.hasGene(ModGenes.CRINGE)) return

        val input = event.message.string
        event.message = Component.literal(uwufyString(input))
    }

    fun handleSlimyChat(event: ServerChatEvent) {
        val slimyDeath = ModGenes.SLIMY_DEATH.getHolder(event.player.registryAccess()) ?: return
        if (slimyDeath.isDisabled) return

        val player = event.player
        if (!player.hasGene(ModGenes.SLIMY_DEATH)) return

        val nearbySupportSlimes = player.level().getEntities(
            player,
            player.boundingBox.inflate(64.0)
        ).filter { it is SupportSlime && it.getOwnerUuid() == player.uuid }

        val amountSlimes = nearbySupportSlimes.size
        val allPlayers = player.server.playerList.players

        for (i in 0 until amountSlimes) {
            val message = Component
                .literal("<")
                .append(
                    ModLanguageProvider.Messages.SLIME_SPAM.toComponent(
                        player.displayName,
                        i + 1
                    )
                )
                .append(Component.literal("> "))
                .append(event.message)

            ModScheduler.scheduleTaskInTicks(i + 1) {
                allPlayers.forEach {
                    it.sendSystemMessage(message)
                }
            }
        }
    }


}