package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

data class ShearedPacket(
    val removingSkin: Boolean
) : ModPacket {

    override fun receiveOnClient(context: IPayloadContext) {
        context.enqueueWork {
            if (removingSkin) {
                ClientUtil.shearPlayerSkin()
            } else {
                ClientUtil.addSkinLayersBack()
            }
        }
    }

    override fun type(): CustomPacketPayload.Type<ShearedPacket> = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<ShearedPacket> =
            CustomPacketPayload.Type<ShearedPacket>(OtherUtil.modResource("sheared"))

        val STREAM_CODEC: StreamCodec<ByteBuf, ShearedPacket> =
            ByteBufCodecs.BOOL.map(::ShearedPacket, ShearedPacket::removingSkin)
    }

}