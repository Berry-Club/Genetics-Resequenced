package dev.aaronhowser.mods.geneticsresequenced.packets.client_to_server

import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator_advanced.AdvancedIncubatorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacket
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class GuiPacket(
    private val x: Int,
    private val y: Int,
    private val z: Int,
    private val bool: Boolean
) : ModPacket {

    constructor(blockPos: BlockPos, bool: Boolean) : this(blockPos.x, blockPos.y, blockPos.z, bool)

    override fun encode(buffer: FriendlyByteBuf) {
        buffer.writeInt(x)
        buffer.writeInt(y)
        buffer.writeInt(z)
        buffer.writeBoolean(bool)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): GuiPacket {
            return GuiPacket(
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readBoolean()
            )
        }
    }

    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        if (context.get().direction != NetworkDirection.PLAY_TO_SERVER) {
            throw IllegalStateException("Received GuiPacket on wrong side!")
        }

        val player = context.get().sender
            ?: throw IllegalStateException("Received GuiPacket without player!")

        val blockEntity = player.level.getBlockEntity(BlockPos(x, y, z))
            ?: throw IllegalStateException("Received GuiPacket with invalid block entity position!")

        if (blockEntity is AdvancedIncubatorBlockEntity) {
            blockEntity.isHighTemperature = bool
        }
    }
}