package dev.aaronhowser.mods.geneticsresequenced.packets

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.packets.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packets.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.EnergySyncPacket
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.GeneChangedPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.simple.SimpleChannel

@Suppress("INACCESSIBLE_TYPE")
object ModPacketHandler {

    private const val PROTOCOL_VERSION = "1"
    private val INSTANCE: SimpleChannel = NetworkRegistry.newSimpleChannel(
        ResourceLocation(GeneticsResequenced.ID, "main"),
        { PROTOCOL_VERSION },
        { anObject: String -> PROTOCOL_VERSION == anObject },
        { anObject: String -> PROTOCOL_VERSION == anObject }
    )

    fun setup() {
        var id = 0

        INSTANCE.registerMessage(
            ++id,
            TeleportPlayerPacket::class.java,
            TeleportPlayerPacket::encode,
            TeleportPlayerPacket::decode,
            TeleportPlayerPacket::receiveMessage
        )

        INSTANCE.registerMessage(
            ++id,
            FireballPacket::class.java,
            FireballPacket::encode,
            FireballPacket::decode,
            FireballPacket::receiveMessage
        )

        INSTANCE.registerMessage(
            ++id,
            GeneChangedPacket::class.java,
            GeneChangedPacket::encode,
            GeneChangedPacket::decode,
            GeneChangedPacket::receiveMessage
        )

        INSTANCE.registerMessage(
            ++id,
            EnergySyncPacket::class.java,
            EnergySyncPacket::encode,
            EnergySyncPacket::decode,
            EnergySyncPacket::receiveMessage
        )

        // INSTANCE.messageBuilder

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