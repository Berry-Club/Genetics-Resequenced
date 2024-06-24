package dev.aaronhowser.mods.geneticsresequenced.packet

import net.neoforged.neoforge.network.handling.IPayloadContext

object ClientPayloadHandler {

    fun handleData(modPacket: ModPacket, context: IPayloadContext) {
        modPacket.receiveMessage(context)
    }

}