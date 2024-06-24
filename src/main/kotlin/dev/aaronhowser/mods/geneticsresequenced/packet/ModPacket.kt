package dev.aaronhowser.mods.geneticsresequenced.packet

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ModPacket : CustomPacketPayload {

    fun receiveMessage(context: IPayloadContext)

}