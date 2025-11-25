package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
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

}