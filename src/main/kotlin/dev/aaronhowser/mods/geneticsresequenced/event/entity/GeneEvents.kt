package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.event.CustomEvents
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.network.chat.HoverEvent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object GeneEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onGeneChanged(event: CustomEvents.GeneChangeEvent.Post) {
        val (livingEntity: LivingEntity, geneHolder: Holder<Gene>, wasAdded: Boolean) = event

        tellAllPlayersGeneChanged(livingEntity, geneHolder, wasAdded)

        geneHolder.value().setAttributeModifiers(livingEntity, wasAdded)

        if (!wasAdded && geneHolder.value().getPotion() != null) {
            TickGenes.handlePotionGeneRemoved(livingEntity, geneHolder)
        }

        if (livingEntity is ServerPlayer) {
            AdvancementTriggers.geneAdvancements(livingEntity, geneHolder, wasAdded)
        }

        ModScheduler.scheduleTaskInTicks(1) {
            checkForMissingRequirements(livingEntity)
        }
    }

    private fun checkForMissingRequirements(entity: LivingEntity) {

        val geneHolders = entity.geneHolders

        val genesWithMissingRequirements = geneHolders.filter { geneHolder ->
            !geneHolder.value().getRequiredGeneHolders(entity.registryAccess()).all { it in geneHolders }
        }

        genesWithMissingRequirements.forEach { geneHolder ->
            entity.removeGene(geneHolder)
            val gene = geneHolder.value()

            val requiredGenesComponent =
                ModLanguageProvider.Messages.MISSING_GENE_REQUIREMENTS_LIST.toComponent()

            val missingGenes = gene.getRequiredGeneHolders(entity.registryAccess()).filter { it !in geneHolders }

            requiredGenesComponent.append(
                OtherUtil.componentList(
                    missingGenes.map { Gene.getNameComponent(it) }
                )
            )

            if (!entity.level().isClientSide) {
                entity.sendSystemMessage(
                    ModLanguageProvider.Messages.MISSING_GENE_REQUIREMENTS
                        .toComponent(Gene.getNameComponent(geneHolder))
                        .withStyle {
                            it.withHoverEvent(
                                HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    requiredGenesComponent
                                )
                            )
                        }
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

        ModPacketHandler.messageAllPlayers(
            GeneChangedPacket(
                entity.id,
                changedGene.key!!.location(),
                wasAdded
            )
        )
    }

}