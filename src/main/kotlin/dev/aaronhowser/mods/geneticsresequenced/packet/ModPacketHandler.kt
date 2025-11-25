package dev.aaronhowser.mods.geneticsresequenced.packet

import dev.aaronhowser.mods.aaron.AaronLib
import dev.aaronhowser.mods.aaron.packet.AaronPacketRegistrar
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.simple.SimpleChannel

object ModPacketHandler : AaronPacketRegistrar() {

	private const val PROTOCOL_VERSION = "1"

	val CHANNEL: SimpleChannel =
		NetworkRegistry.newSimpleChannel(
			AaronLib.modResource("main"),
			{ PROTOCOL_VERSION },
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
		)

	override fun getChannel(): SimpleChannel = CHANNEL

}