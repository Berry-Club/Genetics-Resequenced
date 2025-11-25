package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import net.minecraft.core.Holder
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.network.NetworkEvent

data class GeneChangedPacket(
	val entityId: Int,
	val geneHolder: Holder<Gene>,
	val wasAdded: Boolean
) : AaronPacket() {

	override fun encode(buffer: FriendlyByteBuf) {
		buffer.writeInt(entityId)
	}

	override fun handleOnClient(context: NetworkEvent.Context) {
		val localPlayer = AaronClientUtil.localPlayer ?: return
		val level = localPlayer.level() ?: return

		val entity = level.getEntity(entityId) as? LivingEntity ?: return

		if (wasAdded) {
			entity.addGene(geneHolder)
		} else {
			entity.removeGene(geneHolder)
		}

		if (localPlayer == entity && geneHolder.`is`(ModGenes.CRINGE)) {
			ClientUtil.handleCringe(wasAdded = wasAdded)
		}

		geneHolder.value().setAttributeModifiers(entity, wasAdded)
	}

	companion object {
		fun decode(buffer: FriendlyByteBuf): GeneChangedPacket {
			val entityId = buffer.readInt()
			val geneId = buffer.readResourceLocation()
			val geneHolder = AaronClientUtil.registryHolderLookup<Gene>(geneId)
			val wasAdded = buffer.readBoolean()
			return GeneChangedPacket(entityId, geneHolder, wasAdded)
		}
	}

}