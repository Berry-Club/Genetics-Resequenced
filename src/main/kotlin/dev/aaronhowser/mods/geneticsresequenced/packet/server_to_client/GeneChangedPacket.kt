package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.network.handling.IPayloadContext

data class GeneChangedPacket(
    val entityId: Int,
    val geneId: Gene,
    val wasAdded: Boolean
) : ModPacket {

    override fun type(): CustomPacketPayload.Type<GeneChangedPacket> = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<GeneChangedPacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("gene_changed"))

        val STREAM_CODEC: StreamCodec<ByteBuf, GeneChangedPacket> = StreamCodec.composite(
            ByteBufCodecs.INT, GeneChangedPacket::entityId,
            Gene.STREAM_CODEC, GeneChangedPacket::geneId,
            ByteBufCodecs.BOOL, GeneChangedPacket::wasAdded,
            ::GeneChangedPacket
        )
    }

    override fun receiveMessage(context: IPayloadContext) {
        if (context.flow() != PacketFlow.CLIENTBOUND) throw IllegalStateException("Received GeneChangedPacket on wrong side!")

        val entity = context.player().level().getEntity(entityId) as? LivingEntity
            ?: throw IllegalStateException("Received GeneChangedPacket with invalid entity id!")

        if (wasAdded) {
            entity.addGene(geneId)
        } else {
            entity.removeGene(geneId)
        }
    }

}