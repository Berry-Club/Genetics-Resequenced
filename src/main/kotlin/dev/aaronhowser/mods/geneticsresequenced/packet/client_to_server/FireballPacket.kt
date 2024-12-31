package dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server

import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.PacketGenes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

class FireballPacket private constructor() : ModPacket {

    override fun receiveMessage(context: IPayloadContext) {
        context.enqueueWork {
            val sender = context.player() as? ServerPlayer ?: return@enqueueWork
            PacketGenes.dragonBreath(sender)
        }
    }

    override fun type(): CustomPacketPayload.Type<FireballPacket> = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<FireballPacket> =
            CustomPacketPayload.Type<FireballPacket>(OtherUtil.modResource("fireball"))

        val INSTANCE = FireballPacket()

        val STREAM_CODEC: StreamCodec<ByteBuf, FireballPacket> = StreamCodec.unit(INSTANCE)
    }

}