package dev.aaronhowser.mods.geneticsresequenced.packet

import dev.aaronhowser.mods.aaron.packet.AaronPacketRegistrar
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.NarratorPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.SetGenesPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.ShearedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel
import java.util.Optional
import java.util.function.Supplier

object ModPacketHandler : AaronPacketRegistrar() {

	private const val PROTOCOL_VERSION = "1"

	var CHANNEL: SimpleChannel =
		NetworkRegistry.newSimpleChannel(
			OtherUtil.modResource("main"),
			{ PROTOCOL_VERSION },
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
		)

	override fun getChannel(): SimpleChannel = CHANNEL

	@Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")
	override fun registerPackets(event: FMLCommonSetupEvent) {

		var i = 0

		fun <MSG> registerMessage(
			messageType: Class<MSG>,
			encoder: (MSG, FriendlyByteBuf) -> Unit,
			decoder: (FriendlyByteBuf) -> MSG,
			messageConsumer: (MSG, Supplier<NetworkEvent.Context>) -> Unit,
			direction: NetworkDirection
		) {
			CHANNEL.registerMessage(
				++i,
				messageType,
				encoder,
				decoder,
				messageConsumer,
				Optional.of(direction)
			)
		}

		// C2S

		registerMessage(
			FireballPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> FireballPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnServer(context) },
			NetworkDirection.PLAY_TO_SERVER
		)

		registerMessage(
			TeleportPlayerPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> TeleportPlayerPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnServer(context) },
			NetworkDirection.PLAY_TO_SERVER
		)

		// S2C

		registerMessage(
			GeneChangedPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> GeneChangedPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnClient(context) },
			NetworkDirection.PLAY_TO_CLIENT
		)

		registerMessage(
			NarratorPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> NarratorPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnClient(context) },
			NetworkDirection.PLAY_TO_CLIENT
		)

		registerMessage(
			SetGenesPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> SetGenesPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnClient(context) },
			NetworkDirection.PLAY_TO_CLIENT
		)

		registerMessage(
			ShearedPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> ShearedPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnClient(context) },
			NetworkDirection.PLAY_TO_CLIENT
		)

	}

}