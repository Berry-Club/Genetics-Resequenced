package dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

data class ShearedPacket(
	val removingSkin: Boolean
) : AaronPacket() {

	override fun handleOnClient(context: IPayloadContext) {
		if (removingSkin) {
			ClientUtil.shearPlayerSkin()
		} else {
			ClientUtil.addSkinLayersBack()
		}
	}

	override fun type(): CustomPacketPayload.Type<ShearedPacket> = TYPE

	companion object {
		val TYPE: CustomPacketPayload.Type<ShearedPacket> =
			CustomPacketPayload.Type<ShearedPacket>(GeneticsResequenced.modId("sheared"))

		val STREAM_CODEC: StreamCodec<ByteBuf, ShearedPacket> =
			ByteBufCodecs.BOOL.map(::ShearedPacket, ShearedPacket::removingSkin)
	}

}