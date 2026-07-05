package dev.aaronhowser.mods.genetics_resequenced.packet

import dev.aaronhowser.mods.aaron.packet.AaronPacketRegistrar
import dev.aaronhowser.mods.genetics_resequenced.packet.client_to_server.FireballPacket
import dev.aaronhowser.mods.genetics_resequenced.packet.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client.NarratorPacket
import dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client.SetGenesPacket
import dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client.ShearedPacket
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

object ModPacketHandler : AaronPacketRegistrar {

	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		val registrar = event.registrar("1")

		toClient(
			registrar,
			GeneChangedPacket.TYPE,
			GeneChangedPacket.STREAM_CODEC
		)

		toClient(
			registrar,
			SetGenesPacket.TYPE,
			SetGenesPacket.STREAM_CODEC
		)

		toClient(
			registrar,
			NarratorPacket.TYPE,
			NarratorPacket.STREAM_CODEC
		)

		toClient(
			registrar,
			ShearedPacket.TYPE,
			ShearedPacket.STREAM_CODEC
		)

		toServer(
			registrar,
			FireballPacket.TYPE,
			FireballPacket.STREAM_CODEC
		)

		toServer(
			registrar,
			TeleportPlayerPacket.TYPE,
			TeleportPlayerPacket.STREAM_CODEC
		)
	}

}