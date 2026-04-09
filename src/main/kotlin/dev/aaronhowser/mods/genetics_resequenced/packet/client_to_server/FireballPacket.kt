package dev.aaronhowser.mods.genetics_resequenced.packet.client_to_server

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.PacketGenes
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

class FireballPacket private constructor() : AaronPacket() {

	override fun handleOnServer(context: IPayloadContext) {
		val sender = context.player() as? ServerPlayer ?: return
		PacketGenes.dragonBreath(sender)
	}

	override fun type(): CustomPacketPayload.Type<FireballPacket> = TYPE

	companion object {
		val TYPE: CustomPacketPayload.Type<FireballPacket> =
			CustomPacketPayload.Type<FireballPacket>(GeneticsResequenced.modResource("fireball"))

		val INSTANCE = FireballPacket()

		val STREAM_CODEC: StreamCodec<ByteBuf, FireballPacket> = StreamCodec.unit(INSTANCE)
	}

}