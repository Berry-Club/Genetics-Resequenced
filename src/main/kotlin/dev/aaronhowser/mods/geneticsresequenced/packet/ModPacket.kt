package dev.aaronhowser.mods.geneticsresequenced.packet

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ModPacket : CustomPacketPayload {

    fun receiveOnClient(context: IPayloadContext) {
        throw UnsupportedOperationException("Packet $this cannot be received on the client!")
    }

    fun receiveOnServer(context: IPayloadContext) {
        throw UnsupportedOperationException("Packet $this cannot be received on the server!")
    }

}