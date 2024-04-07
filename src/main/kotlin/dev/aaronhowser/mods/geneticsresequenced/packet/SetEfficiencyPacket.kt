package dev.aaronhowser.mods.geneticsresequenced.packet

import dev.aaronhowser.mods.geneticsresequenced.attribute.ModAttributes
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class SetEfficiencyPacket(
    private val newLevel: Int
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeInt(newLevel)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): SetEfficiencyPacket {
            return SetEfficiencyPacket(buffer.readInt())
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {

        if (context.get().direction != NetworkDirection.PLAY_TO_CLIENT) {
            throw IllegalStateException("Received SetEfficiencyPacket on wrong side!")
        }

        val player = Minecraft.getInstance().player
            ?: throw IllegalStateException("Received SetEfficiencyPacket without player!")

        player.attributes.getInstance(ModAttributes.EFFICIENCY)?.baseValue = newLevel.toDouble()

    }
}