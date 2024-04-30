package dev.aaronhowser.mods.geneticsresequenced.packets

import dev.aaronhowser.mods.geneticsresequenced.packets.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packets.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.EnergySyncPacket
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.ShearedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel

object ModPacketHandler {

    private const val PROTOCOL_VERSION = "1"
    private val INSTANCE: SimpleChannel = NetworkRegistry.newSimpleChannel(
        OtherUtil.modResource("main"),
        { PROTOCOL_VERSION },
        { anObject: String -> PROTOCOL_VERSION == anObject },
        { anObject: String -> PROTOCOL_VERSION == anObject }
    )

    private var id = 0
    private inline fun <reified T : ModPacket> buildMessage(
        packetClass: Class<T> = T::class.java,
        direction: NetworkDirection,
        crossinline decoder: (buffer: FriendlyByteBuf) -> T,
    ) {

        INSTANCE.messageBuilder(packetClass, ++id, direction)
            .encoder { packet, buffer -> packet.encode(buffer) }
            .decoder { buffer -> decoder(buffer) }
            .consumerMainThread { packet, context -> packet.receiveMessage(context) }
            .add()

    }

    fun setup() {

        buildMessage<TeleportPlayerPacket>(
            direction = NetworkDirection.PLAY_TO_SERVER,
            decoder = TeleportPlayerPacket::decode
        )

        buildMessage<FireballPacket>(
            direction = NetworkDirection.PLAY_TO_SERVER,
            decoder = FireballPacket::decode
        )

        buildMessage<EnergySyncPacket>(
            direction = NetworkDirection.PLAY_TO_CLIENT,
            decoder = EnergySyncPacket::decode
        )

        buildMessage<GeneChangedPacket>(
            direction = NetworkDirection.PLAY_TO_CLIENT,
            decoder = GeneChangedPacket::decode
        )

        buildMessage<ShearedPacket>(
            direction = NetworkDirection.PLAY_TO_CLIENT,
            decoder = ShearedPacket::decode
        )
    }

    fun messageNearbyPlayers(packet: ModPacket, serverLevel: ServerLevel, origin: Vec3, radius: Double) {
        for (player in serverLevel.players()) {
            val distance = player.distanceToSqr(origin.x(), origin.y(), origin.z())
            if (distance < radius * radius) {
                messagePlayer(player, packet)
            }
        }
    }

    fun messagePlayer(player: ServerPlayer, packet: ModPacket) {
        INSTANCE.send(
            PacketDistributor.PLAYER.with { player },
            packet
        )
    }

    fun messageAllPlayers(packet: ModPacket) {
        INSTANCE.send(
            PacketDistributor.ALL.noArg(),
            packet
        )
    }

    fun messageServer(packet: ModPacket) {
        INSTANCE.sendToServer(packet)
    }

}