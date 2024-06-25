package dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server

import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.PacketGenes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

class TeleportPlayerPacket : ModPacket {
    override fun receiveMessage(context: IPayloadContext) {
        val sender = context.player() as? ServerPlayer
            ?: throw IllegalStateException("Received TeleportPlayerPacket on wrong side!")
        PacketGenes.teleport(sender)
    }

    override fun type(): CustomPacketPayload.Type<TeleportPlayerPacket> = TYPE

    override fun equals(other: Any?): Boolean = other is TeleportPlayerPacket

    companion object {
        val TYPE: CustomPacketPayload.Type<TeleportPlayerPacket> =
            CustomPacketPayload.Type<TeleportPlayerPacket>(OtherUtil.modResource("teleport"))

        val STREAM_CODEC: StreamCodec<ByteBuf, TeleportPlayerPacket> = StreamCodec.unit(TeleportPlayerPacket())
    }

}