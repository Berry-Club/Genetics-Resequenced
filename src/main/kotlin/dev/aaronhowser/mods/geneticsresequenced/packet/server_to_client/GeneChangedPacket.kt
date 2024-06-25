package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.network.handling.IPayloadContext

data class GeneChangedPacket(
    val entityId: Int,
    val geneRl: ResourceLocation,
    val wasAdded: Boolean
) : ModPacket {

    override fun type(): CustomPacketPayload.Type<GeneChangedPacket> = TYPE

    override fun receiveMessage(context: IPayloadContext) {
        require(context.flow() == PacketFlow.CLIENTBOUND) { "Received GeneChangedPacket on wrong side!" }

        val entity = context.player().level().getEntity(entityId) as? LivingEntity
        requireNotNull(entity) { "Received GeneChangedPacket with invalid entity id!" }

        val gene = Gene.fromId(geneRl)
            ?: throw IllegalStateException("Received GeneChangedPacket with invalid gene id!")

        if (wasAdded) {
            entity.addGene(gene)
        } else {
            entity.removeGene(gene)
        }

        if (gene == ModGenes.cringe) ClientUtil.handleCringe(wasAdded)

        handleAttributes(gene)
    }

    private fun handleAttributes(gene: Gene) {
        val player = ClientUtil.localPlayer ?: throw IllegalStateException("Received GeneChangedPacket without player!")

        val attributeInstance = when (gene) {
            ModGenes.efficiency, ModGenes.efficiencyFour -> player.attributes.getInstance(ModAttributes.EFFICIENCY)
            ModGenes.wallClimbing -> player.attributes.getInstance(ModAttributes.WALL_CLIMBING)
            else -> null
        } ?: return

        val newLevel = when (gene) {
            ModGenes.efficiency -> if (wasAdded) 1.0 else 0.0

            ModGenes.efficiencyFour -> {
                if (wasAdded) {
                    4.0
                } else {
                    if (player.hasGene(ModGenes.efficiency)) {
                        1.0
                    } else {
                        0.0
                    }
                }
            }

            ModGenes.wallClimbing -> if (wasAdded) 1.0 else 0.0

            else -> throw IllegalStateException("Gene $gene went through the GeneChangedPacket but isn't handled!")
        }

        attributeInstance.baseValue = newLevel

    }

    companion object {
        val TYPE: CustomPacketPayload.Type<GeneChangedPacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("gene_changed"))

        val STREAM_CODEC: StreamCodec<ByteBuf, GeneChangedPacket> = StreamCodec.composite(
            ByteBufCodecs.INT, GeneChangedPacket::entityId,
            ResourceLocation.STREAM_CODEC, GeneChangedPacket::geneRl,
            ByteBufCodecs.BOOL, GeneChangedPacket::wasAdded,
            ::GeneChangedPacket
        )
    }

}