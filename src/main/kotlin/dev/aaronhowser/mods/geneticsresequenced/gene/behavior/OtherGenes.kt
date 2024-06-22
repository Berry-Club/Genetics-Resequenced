package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneContainer.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import net.minecraft.network.chat.Component
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
        if (!ModGenes.emeraldHeart.isActive) return

        if (Random.nextDouble() > ServerConfig.emeraldHeartChatChance.get()) return

        val player = event.player

        if (player.hasGene(ModGenes.emeraldHeart)) {
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
        if (!ModGenes.chatterbox.isActive) return

        val player = event.player
        if (!player.hasGene(ModGenes.chatterbox)) return

        val message = event.message

        //TODO
//        ModPacketHandler.messageNearbyPlayers(
//            NarratorPacket(message.string),
//            player.level() as ServerLevel,
//            player.position(),
//            64.0
//        )
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
            input = input.toUpperCase()
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
        if (!ModGenes.cringe.isActive) return

        val player = event.player
        if (!player.hasGene(ModGenes.cringe)) return

        val input = event.message.string
        event.message = Component.literal(uwufyString(input))
    }

    //TODO
//    fun handleSlimyChat(event: ServerChatEvent) {
//        if (!ModGenes.slimyDeath.isActive) return
//
//        val player = event.player
//        if (!player.hasGene(ModGenes.slimyDeath)) return
//
//        val nearbySupportSlimes = player.level().getEntities(
//            player,
//            player.boundingBox.inflate(64.0)
//        ).filter { it is SupportSlime && it.getOwnerUuid() == player.uuid }
//
//        val amountSlimes = nearbySupportSlimes.size
//        val allPlayers = player.server.playerList.players
//
//        for (i in 0 until amountSlimes) {
//            val message = Component
//                .literal("<")
//                .append(Component.translatable("message.geneticsresequenced.slimy_spam", player.displayName, i + 1))
//                .append(Component.literal("> "))
//                .append(event.message)
//
//            ModScheduler.scheduleTaskInTicks(i + 1) {
//                allPlayers.forEach {
//                    it.sendSystemMessage(message)
//                }
//            }
//        }
//    }


}