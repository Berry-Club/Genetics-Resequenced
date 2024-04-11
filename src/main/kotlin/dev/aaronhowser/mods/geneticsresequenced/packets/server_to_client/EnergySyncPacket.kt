package dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.blockentities.CellAnalyzerBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blockentities.CoalGeneratorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.screens.CellAnalyzerMenu
import dev.aaronhowser.mods.geneticsresequenced.screens.CoalGeneratorMenu
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
        buffer.writeInt(energy)
        buffer.writeBlockPos(pos)
    }

    companion object {
        fun decode(buffer: FriendlyByteBuf): EnergySyncPacket {
            return EnergySyncPacket(
                buffer.readInt(),
                buffer.readBlockPos()
            )
        }
    }

    @Suppress("MoveVariableDeclarationIntoWhen")
    override fun receiveMessage(context: Supplier<NetworkEvent.Context>) {
        if (context.get().direction != NetworkDirection.PLAY_TO_CLIENT) {
            throw IllegalStateException("Received SetWallClimbingPacket on wrong side!")
        }

        val blockEntity = Minecraft.getInstance().level?.getBlockEntity(pos)

        when (blockEntity) {
            is CellAnalyzerBlockEntity -> handleCellAnalyzer(blockEntity)
            is CoalGeneratorBlockEntity -> handleCoalGenerator(blockEntity)
        }

    }

    private fun handleCellAnalyzer(blockEntity: CellAnalyzerBlockEntity) {
        blockEntity.setEnergy(energy)
        val playerMenu = Minecraft.getInstance().player?.containerMenu
        // don't really understand why this one is here, kaupenjoe says it's needed so like whatever
        if (playerMenu is CellAnalyzerMenu && playerMenu.blockEntity.blockPos == pos) {
            blockEntity.setEnergy(energy)
        }
    }

    private fun handleCoalGenerator(blockEntity: CoalGeneratorBlockEntity) {
        blockEntity.setEnergy(energy)
        val playerMenu = Minecraft.getInstance().player?.containerMenu
        if (playerMenu is CoalGeneratorMenu && playerMenu.blockEntity.blockPos == pos) {
            blockEntity.setEnergy(energy)
        }
    }
}