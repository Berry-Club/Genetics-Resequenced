package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
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
) : AaronPacket() {

	override fun handleOnClient(context: IPayloadContext) {
		val level = context.player().level()
		val entity = level.getEntity(entityId) as? LivingEntity ?: return

		if (this.wasAdded) {
			entity.addGene(this.geneHolder)
		} else {
			entity.removeGene(this.geneHolder)
		}

		if (this.geneHolder.isGene(ModGenes.CRINGE)) ClientUtil.handleCringe(this.wasAdded)

		this.geneHolder.value().setAttributeModifiers(entity, this.wasAdded)
	}

	override fun type(): CustomPacketPayload.Type<GeneChangedPacket> = TYPE

	companion object {
		val TYPE: CustomPacketPayload.Type<GeneChangedPacket> =
			CustomPacketPayload.Type(GeneticsResequenced.modResource("gene_changed"))

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GeneChangedPacket> =
			StreamCodec.composite(
				ByteBufCodecs.INT, GeneChangedPacket::entityId,
				Gene.STREAM_CODEC, GeneChangedPacket::geneHolder,
				ByteBufCodecs.BOOL, GeneChangedPacket::wasAdded,
				::GeneChangedPacket
			)
	}

}