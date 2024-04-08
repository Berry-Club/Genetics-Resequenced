package dev.aaronhowser.mods.geneticsresequenced.packets

import dev.aaronhowser.mods.geneticsresequenced.genebehaviors.PacketGenes
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class TeleportPlayerPacket : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): TeleportPlayerPacket {
            return TeleportPlayerPacket()
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        val player = context.get().sender ?: return
        PacketGenes.teleport(player)

        context.get().packetHandled = true
    }

}