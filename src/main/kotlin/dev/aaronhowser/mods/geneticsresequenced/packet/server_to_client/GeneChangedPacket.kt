package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.network.handling.IPayloadContext

data class GeneChangedPacket(
    val entityId: Int,
    val geneHolder: Holder<Gene>,
    val wasAdded: Boolean
) : ModPacket {

    override fun receiveOnClient(context: IPayloadContext) {
        context.enqueueWork {
            // Return if the entity does not exist on the client. This happens when the entity is not being tracked on the client, aka if it's too far away or whatever.
            val entity = context.player().level().getEntity(entityId) as? LivingEntity ?: return@enqueueWork

            if (wasAdded) {
                entity.addGene(geneHolder)
            } else {
                entity.removeGene(geneHolder)
            }

            if (geneHolder.isGene(ModGenes.CRINGE)) ClientUtil.handleCringe(wasAdded)

            geneHolder.value().setAttributeModifiers(entity, wasAdded)
        }
    }

    override fun type(): CustomPacketPayload.Type<GeneChangedPacket> = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<GeneChangedPacket> =
            CustomPacketPayload.Type(OtherUtil.modResource("gene_changed"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GeneChangedPacket> =
            StreamCodec.composite(
                ByteBufCodecs.INT, GeneChangedPacket::entityId,
                Gene.STREAM_CODEC, GeneChangedPacket::geneHolder,
                ByteBufCodecs.BOOL, GeneChangedPacket::wasAdded,
                ::GeneChangedPacket
            )
    }

}