package dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent

data class ShearedPacket(
	val removingSkin: Boolean
) : AaronPacket() {

	override fun encode(buffer: FriendlyByteBuf) {
		buffer.writeBoolean(this.removingSkin)
	}

	override fun handleOnClient(context: NetworkEvent.Context) {
		if (this.removingSkin) {
			ClientUtil.shearPlayerSkin()
		} else {
			ClientUtil.addSkinLayersBack()
		}
	}

	companion object {
		fun decode(buffer: FriendlyByteBuf): ShearedPacket {
			val removingSkin = buffer.readBoolean()
			return ShearedPacket(removingSkin)
		}
	}

}