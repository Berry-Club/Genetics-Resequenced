package dev.aaronhowser.mods.geneticsresequenced.packets.client_to_server

import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.PacketGenes
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacket
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class FireballPacket : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): FireballPacket {
            return FireballPacket()
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        val player = context.get().sender ?: return
        PacketGenes.dragonBreath(player)

        context.get().packetHandled = true
    }

}