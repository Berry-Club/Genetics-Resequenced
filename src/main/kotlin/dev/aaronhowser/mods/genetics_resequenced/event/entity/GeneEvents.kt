package dev.aaronhowser.mods.genetics_resequenced.event.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withHoverText
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.genetics_resequenced.data.GeneRequirements
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.event.custom.GeneChangeEvent
import dev.aaronhowser.mods.genetics_resequenced.event.custom.GeneCooldownEvent
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client.ShearedPacket
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.network.chat.Style
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID
)
object GeneEvents {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun afterGeneChanged(event: GeneChangeEvent.Post) {
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
				entity.tell(ModMessageLang.MISSING_GENE_REQUIREMENTS_1.toComponent(geneHolder.getName()))
				entity.tell(
					ModMessageLang.MISSING_GENE_REQUIREMENTS_2.toComponent()
						.withStyle(Style.EMPTY.withHoverText(requiredGenesComponent))
				)
			}
		}
	}

	private fun tellAllPlayersGeneChanged(entity: LivingEntity, changedGene: Holder<Gene>, wasAdded: Boolean) {
		if (entity.level().isClientSide) return

		val server = entity.level().server
		if (server == null) {
			GeneticsResequenced.LOGGER.error("Server is null when trying to tell all players about gene change")
			return
		}

		val packet = GeneChangedPacket(entity.id, changedGene, wasAdded)
		packet.messageAllPlayers()
	}

	@SubscribeEvent
	fun onCooldownEnded(event: GeneCooldownEvent.Remove) {
		val (entity, geneHolder) = event

		if (entity is ServerPlayer && geneHolder.isGene(ModGenes.WOOLY)) {
			val packet = ShearedPacket(removingSkin = false)
			packet.messagePlayer(entity)
		}
	}

}
