package dev.aaronhowser.mods.geneticsresequenced.packet

import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.NarratorPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.SetGenesPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.ShearedPacket
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object ModPacketHandler {

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

    fun messageNearbyPlayers(packet: ModPacket, serverLevel: ServerLevel, origin: Vec3, radius: Double) {
        for (player in serverLevel.players()) {
            val distance = player.distanceToSqr(origin.x(), origin.y(), origin.z())
            if (distance < radius * radius) {
                messagePlayer(player, packet)
            }
        }
    }

    fun messagePlayer(player: ServerPlayer, packet: ModPacket) {
        PacketDistributor.sendToPlayer(player, packet)
    }

    fun messageAllPlayers(packet: ModPacket) {
        PacketDistributor.sendToAllPlayers(packet)
    }

    fun messageServer(packet: ModPacket) {
        PacketDistributor.sendToServer(packet)
    }

    private fun <T : ModPacket> toClient(
        registrar: PayloadRegistrar,
        packetType: CustomPacketPayload.Type<T>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>,
    ) {
        registrar.playToClient(
            packetType,
            streamCodec
        ) { packet, context -> packet.receiveOnClient(context) }
    }

    private fun <T : ModPacket> toServer(
        registrar: PayloadRegistrar,
        packetType: CustomPacketPayload.Type<T>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
    ) {
        registrar.playToServer(
            packetType,
            streamCodec
        ) { packet, context -> packet.receiveOnServer(context) }
    }

}