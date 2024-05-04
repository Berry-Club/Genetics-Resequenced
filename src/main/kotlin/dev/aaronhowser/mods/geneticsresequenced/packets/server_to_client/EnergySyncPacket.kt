package dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.blocks.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

class EnergySyncPacket(
    private val energy: Int,
    private val pos: BlockPos
) : ModPacket {

    override fun encode(buffer: FriendlyByteBuf) {
        println("Encoding a packet with energy: $energy and pos: $pos")
        buffer.writeInt(energy)
        buffer.writeBlockPos(pos)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): EnergySyncPacket {
            val energy = buffer.readInt()
            val pos = buffer.readBlockPos()
            println("Decoding a packet with energy: $energy and pos: $pos")
            return EnergySyncPacket(energy, pos)
        }
    }

    @Suppress("MoveVariableDeclarationIntoWhen")
    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        if (context.get().direction != NetworkDirection.PLAY_TO_CLIENT) {
            throw IllegalStateException("Received SetWallClimbingPacket on wrong side!")
        }

        val blockEntity = Minecraft.getInstance().level?.getBlockEntity(pos)

        when (blockEntity) {
            is InventoryEnergyBlockEntity -> handleEnergyMenu(blockEntity)
        }

    }

    private fun handleEnergyMenu(blockEntity: InventoryEnergyBlockEntity) {
        blockEntity.setClientEnergy(energy)
        val playerMenu = Minecraft.getInstance().player?.containerMenu
        if (playerMenu is MachineMenu && playerMenu.blockEntity.blockPos == pos) {
            blockEntity.setClientEnergy(energy)
        }
    }
}