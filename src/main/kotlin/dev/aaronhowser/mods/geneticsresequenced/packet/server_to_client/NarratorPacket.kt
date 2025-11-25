package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent

data class NarratorPacket(
	val message: String
) : AaronPacket() {

	override fun encode(buffer: FriendlyByteBuf) {
		buffer.writeUtf(this.message)
	}

	override fun handleOnClient(context: NetworkEvent.Context) {
		if (ClientConfig.CONFIG.disableParrotNarrator.get()) return
		Minecraft.getInstance().narrator.narrator.say(this.message, true)
	}

	companion object {
		fun decode(buffer: FriendlyByteBuf): NarratorPacket {
			val message = buffer.readUtf()
			return NarratorPacket(message)
		}
	}

}