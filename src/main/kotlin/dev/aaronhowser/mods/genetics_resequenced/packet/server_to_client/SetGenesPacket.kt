package dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.removeAllGenes
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.network.handling.IPayloadContext

data class SetGenesPacket(
	val entityId: Int,
	val geneSet: HolderSet<Gene>
) : AaronPacket() {

	constructor(
		entityId: Int,
		genes: Collection<Holder<Gene>>
	) : this(entityId, HolderSet.direct(*genes.toTypedArray()))

	override fun handleOnClient(context: IPayloadContext) {
		val level = context.player().level()
		val entity = level.getEntity(entityId) as? LivingEntity ?: return

		entity.removeAllGenes()

		for (gene in geneSet) {
			entity.addGene(gene)
		}
	}

	override fun type(): CustomPacketPayload.Type<SetGenesPacket> {
		return TYPE
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<SetGenesPacket> =
			CustomPacketPayload.Type(GeneticsResequenced.modResource("set_genes"))

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetGenesPacket> =
			StreamCodec.composite(
				ByteBufCodecs.INT, SetGenesPacket::entityId,
				ByteBufCodecs.holderSet(ModGenes.GENE_REGISTRY_KEY), SetGenesPacket::geneSet,
				::SetGenesPacket
			)
	}

}