package dev.aaronhowser.mods.geneticsresequenced.packet

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
        ResourceLocation("pitchperfect", "main"),
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
            SetEfficiencyPacket::class.java,
            SetEfficiencyPacket::encode,
            SetEfficiencyPacket::decode,
            SetEfficiencyPacket::receiveMessage
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

    fun messageServer(packet: ModPacket) {
        INSTANCE.sendToServer(packet)
    }

}