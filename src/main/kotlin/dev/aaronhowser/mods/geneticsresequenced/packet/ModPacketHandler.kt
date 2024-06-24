package dev.aaronhowser.mods.geneticsresequenced.packet

import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler

object ModPacketHandler {

    fun registerPayloads(event: RegisterPayloadHandlersEvent) {

        val registrar = event.registrar("1")

        registrar.playToClient(
            GeneChangedPacket.TYPE,
            GeneChangedPacket.STREAM_CODEC,
            DirectionalPayloadHandler(
                { packet, context -> ClientPayloadHandler.handleData(packet, context) },
                { packet, context -> ServerPayloadHandler.handleData(packet, context) }
            )
        )

    }

}