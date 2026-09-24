package dev.aaronhowser.mods.genetics_resequenced.packet.client_to_server

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.PacketGenes
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent

class FireballPacket private constructor() : AaronPacket() {

	override fun encode(buffer: FriendlyByteBuf) {
		// No data to encode
	}

	override fun handleOnServer(context: NetworkEvent.Context) {
		val player = context.sender ?: return
		PacketGenes.dragonBreath(player)
	}

	companion object {
		val INSTANCE = FireballPacket()

		fun decode(buffer: FriendlyByteBuf): FireballPacket = INSTANCE
	}

}