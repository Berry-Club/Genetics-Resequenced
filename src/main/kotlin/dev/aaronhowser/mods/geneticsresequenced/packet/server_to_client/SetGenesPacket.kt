package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeAllGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.network.NetworkEvent

data class SetGenesPacket(
	val entityId: Int,
	val geneRls: List<ResourceLocation>
) : AaronPacket() {

	override fun handleOnClient(context: NetworkEvent.Context) {
		val sender = context.sender ?: return
		val level = sender.level()
		val entity = level.getEntity(this.entityId) as? LivingEntity ?: return

		val geneRegistry = level.registryAccess().registryOrThrow(ModGenes.GENE_REGISTRY_KEY)

		entity.removeAllGenes()

		for (geneRl in this.geneRls) {
			val geneRk = ResourceKey.create(ModGenes.GENE_REGISTRY_KEY, geneRl)
			val geneHolder = geneRegistry.getHolderOrThrow(geneRk)
			entity.addGene(geneHolder)
		}
	}

	override fun encode(buffer: FriendlyByteBuf) {
		buffer.writeInt(this.entityId)

		for (gene in geneRls) {
			buffer.writeResourceLocation(gene)
		}
	}

	companion object {
		fun decode(buffer: FriendlyByteBuf): SetGenesPacket {
			val entityId = buffer.readInt()
			val geneRls = mutableListOf<ResourceLocation>()

			while (buffer.isReadable) {
				geneRls.add(buffer.readResourceLocation())
			}

			return SetGenesPacket(entityId, geneRls)
		}
	}

}