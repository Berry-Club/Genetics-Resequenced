package dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.PacketGenes
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent

class TeleportPlayerPacket private constructor() : AaronPacket() {

	override fun encode(buffer: FriendlyByteBuf) {
		// No data to encode
	}

	override fun handleOnServer(context: NetworkEvent.Context) {
		val player = context.sender ?: return
		PacketGenes.teleport(player)
	}

	companion object {
		val INSTANCE = TeleportPlayerPacket()
		fun decode(buffer: FriendlyByteBuf): TeleportPlayerPacket = INSTANCE
	}

}