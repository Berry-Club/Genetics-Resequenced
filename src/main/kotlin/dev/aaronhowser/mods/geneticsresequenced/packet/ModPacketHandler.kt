package dev.aaronhowser.mods.geneticsresequenced.packet

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.packet.AaronPacketRegistrar
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.NarratorPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.ShearedPacket
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.network.NetworkEvent
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel
import java.util.function.Supplier

object ModPacketHandler : AaronPacketRegistrar() {

	private const val PROTOCOL_VERSION = "1"

	val CHANNEL: SimpleChannel =
		NetworkRegistry.ChannelBuilder
			.named(AaronLib.modResource("main"))
			.networkProtocolVersion { PROTOCOL_VERSION }
			.clientAcceptedVersions { it == PROTOCOL_VERSION }
			.serverAcceptedVersions { it == PROTOCOL_VERSION }
			.simpleChannel()

	override fun getChannel(): SimpleChannel = CHANNEL

	@Suppress("INFERRED_INVISIBLE_RETURN_TYPE_WARNING")
	override fun registerPackets(event: FMLCommonSetupEvent) {
		var i = 0

		fun <MSG> registerMessage(
			messageType: Class<MSG>,
			encoder: (MSG, FriendlyByteBuf) -> Unit,
			decoder: (FriendlyByteBuf) -> MSG,
			messageConsumer: (MSG, Supplier<NetworkEvent.Context>) -> Unit
		) {
			CHANNEL.registerMessage(
				++i,
				messageType,
				encoder,
				decoder,
				messageConsumer
			)
		}

		// C2S

		registerMessage(
			FireballPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> FireballPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnServer(context) }
		)

		registerMessage(
			TeleportPlayerPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> TeleportPlayerPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnServer(context) }
		)

		// S2C

		registerMessage(
			NarratorPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> NarratorPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnClient(context) }
		)

		registerMessage(
			ShearedPacket::class.java,
			{ packet, buffer -> packet.encode(buffer) },
			{ buffer -> ShearedPacket.decode(buffer) },
			{ packet, context -> packet.receiveOnClient(context) }
		)

	}

}