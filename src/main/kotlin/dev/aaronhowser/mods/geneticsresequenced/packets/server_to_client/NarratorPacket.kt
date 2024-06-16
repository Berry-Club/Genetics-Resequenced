package dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.configs.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class NarratorPacket(
    val message: String
) : ModPacket {
    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeUtf(message)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): NarratorPacket {
            return NarratorPacket(
                buffer.readUtf()
            )
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        require(context.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            "Received NarratorPacket on wrong side!"
        }

        if (ClientConfig.disableParrotNarrator.get()) return

        Minecraft.getInstance().narrator.narrator.say(
            message,
            true
        )
    }
}