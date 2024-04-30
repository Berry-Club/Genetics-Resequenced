package dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ClientHelper
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

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        require(context.get().direction == NetworkDirection.PLAY_TO_CLIENT) {
            "Received ShearedPacket on wrong side!"
        }

        if (removingSkin) {
            ClientHelper.shearPlayerSkin()
        } else {
            ClientHelper.addSkinLayersBack()
        }
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): ShearedPacket {
            return ShearedPacket(removingSkin = buffer.readBoolean())
        }
    }
}