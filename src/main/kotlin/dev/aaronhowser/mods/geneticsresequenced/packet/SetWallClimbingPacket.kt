package dev.aaronhowser.mods.geneticsresequenced.packet

import dev.aaronhowser.mods.geneticsresequenced.attribute.ModAttributes
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class SetWallClimbingPacket(
    private val enabled: Boolean
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeBoolean(enabled)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): SetWallClimbingPacket {
            return SetWallClimbingPacket(buffer.readBoolean())
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {

        if (context.get().direction != NetworkDirection.PLAY_TO_CLIENT) {
            throw IllegalStateException("Received SetWallClimbingPacket on wrong side!")
        }

        val player = Minecraft.getInstance().player
            ?: throw IllegalStateException("Received SetWallClimbingPacket without player!")

        player.attributes.getInstance(ModAttributes.WALL_CLIMBING)?.baseValue = if (enabled) 1.0 else 0.0

    }
}