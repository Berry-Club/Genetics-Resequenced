package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeAllGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.network.NetworkEvent

data class SetGenesPacket(
	val entityId: Int,
	val geneSet: HolderSet<Gene>
) : AaronPacket() {

	constructor(
		entityId: Int,
		genes: Collection<Holder<Gene>>
	) : this(entityId, HolderSet.direct(*genes.toTypedArray()))

	override fun handleOnClient(context: NetworkEvent.Context) {
		val sender = context.sender ?: return
		val level = sender.level()
		val entity = level.getEntity(this.entityId) as? LivingEntity ?: return

		entity.removeAllGenes()

		for (gene in this.geneSet) {
			entity.addGene(gene)
		}
	}

	override fun encode(buffer: FriendlyByteBuf) {
		buffer.writeInt(this.entityId)

		for (gene in geneSet) {
			buffer.writeRegistryId(ModGenes.GENE_REGISTRY_KEY, gene)
		}
	}

}