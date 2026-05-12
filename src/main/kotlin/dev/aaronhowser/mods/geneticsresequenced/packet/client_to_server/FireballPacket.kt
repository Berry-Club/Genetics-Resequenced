package dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.PacketGenes
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
		val INSTANCE = FireballPacket()

		val TYPE: CustomPacketPayload.Type<FireballPacket> =
			CustomPacketPayload.Type<FireballPacket>(GeneticsResequenced.modResource("fireball"))

		val STREAM_CODEC: StreamCodec<ByteBuf, FireballPacket> = StreamCodec.unit(INSTANCE)
	}

}