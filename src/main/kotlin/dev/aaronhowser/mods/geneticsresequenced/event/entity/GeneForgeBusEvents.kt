package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getLocationOrNull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.registryAccess
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withHoverText
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.permanentGeneHolders
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.data.GeneRequirements
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneChangeEventPost
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.network.chat.Style
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.FORGE
)
object GeneForgeBusEvents {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun afterGeneChanged(event: GeneChangeEventPost) {
		val (livingEntity: LivingEntity, geneHolder: Holder<Gene>, wasAdded: Boolean) = event

		tellAllPlayersGeneChanged(livingEntity, geneHolder, wasAdded)

		geneHolder.value().setAttributeModifiers(livingEntity, wasAdded)

		if (!wasAdded && geneHolder.value().potions.isNotEmpty()) {
			TickGenes.handlePotionGeneRemoved(livingEntity, geneHolder)
		}

		if (livingEntity is ServerPlayer) {
			AdvancementTriggers.geneAdvancements(livingEntity, geneHolder, wasAdded)
		}

		livingEntity.level().scheduleTaskInTicks(1) {
			checkForMissingRequirements(livingEntity)
		}
	}

	private fun checkForMissingRequirements(entity: LivingEntity) {
		val entityGeneHolders = entity.permanentGeneHolders

		for (geneHolder in entityGeneHolders) {
			val genesWithMissingRequirements = GeneRequirements.getRequiredGeneHolders(
				geneHolder,
				entity.registryAccess()
			).filter { it !in entityGeneHolders }

			if (genesWithMissingRequirements.isEmpty()) continue

			entity.removeGene(geneHolder)

			val requiredGenesComponent =
				ModMessageLang.MISSING_GENE_REQUIREMENTS_LIST.toComponent()

			val missingGenes = GeneRequirements.getRequiredGeneHolders(
				geneHolder,
				entity.registryAccess()
			).filter { it !in entityGeneHolders }

			requiredGenesComponent.append(
				OtherUtil.componentList(
					missingGenes.map(Gene::getNameComponent)
				)
			)

			if (!entity.level().isClientSide) {
				entity.sendSystemMessage(
					ModMessageLang.MISSING_GENE_REQUIREMENTS
						.toComponent(geneHolder.getName())
						.withStyle(
							Style.EMPTY.withHoverText(requiredGenesComponent)
						)
				)
			}
		}
	}

	private fun tellAllPlayersGeneChanged(entity: LivingEntity, changedGene: Holder<Gene>, wasAdded: Boolean) {
		if (entity.level().isClientSide) return

		val server = entity.server
		if (server == null) {
			GeneticsResequenced.LOGGER.error("Server is null when trying to tell all players about gene change")
			return
		}

		val rl = changedGene.getLocationOrNull() ?: return

		val packet = GeneChangedPacket(entity.id, rl, wasAdded)
		ModPacketHandler.messageAllPlayersTrackingEntityAndSelf(packet, entity)
	}

}