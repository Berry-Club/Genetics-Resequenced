package dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class ShearedPacket(
    val removingSkin: Boolean
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeBoolean(removingSkin)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): ShearedPacket {
            return ShearedPacket(
                buffer.readBoolean()
            )
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        require(context.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            "Received ShearedPacket on wrong side!"
        }

        if (removingSkin) {
            ClientUtil.shearPlayerSkin()
        } else {
            ClientUtil.addSkinLayersBack()
        }

        context.get().packetHandled = true
    }
}