package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

data class NarratorPacket(
    val message: String
) : ModPacket {

    override fun receiveOnClient(context: IPayloadContext) {
        context.enqueueWork {
            if (ClientConfig.disableParrotNarrator.get()) return@enqueueWork

            Minecraft.getInstance().narrator.narrator.say(message, true)
        }
    }

    override fun type(): CustomPacketPayload.Type<NarratorPacket> = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<NarratorPacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("narrator"))

        val STREAM_CODEC: StreamCodec<ByteBuf, NarratorPacket> =
            ByteBufCodecs.STRING_UTF8.map(::NarratorPacket, NarratorPacket::message)

    }
}