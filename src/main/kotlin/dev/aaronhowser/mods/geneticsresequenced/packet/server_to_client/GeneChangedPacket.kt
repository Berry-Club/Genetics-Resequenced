package dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.network.NetworkEvent

data class GeneChangedPacket(
	val entityId: Int,
	val geneRl: ResourceLocation,
	val wasAdded: Boolean
) : AaronPacket() {

	override fun encode(buffer: FriendlyByteBuf) {
		buffer.writeInt(entityId)
	}

	override fun handleOnClient(context: NetworkEvent.Context) {
		val localPlayer = AaronClientUtil.localPlayer ?: return
		val level = localPlayer.level() ?: return

		val entity = level.getEntity(entityId) as? LivingEntity ?: return

		val geneRk = ResourceKey.create(ModGenes.GENE_REGISTRY_KEY, geneRl)
		val geneRegistry = level.registryAccess().registryOrThrow(ModGenes.GENE_REGISTRY_KEY)
		val geneHolder = geneRegistry.getHolderOrThrow(geneRk)

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
			val geneRl = buffer.readResourceLocation()
			val wasAdded = buffer.readBoolean()
			return GeneChangedPacket(entityId, geneRl, wasAdded)
		}
	}

}