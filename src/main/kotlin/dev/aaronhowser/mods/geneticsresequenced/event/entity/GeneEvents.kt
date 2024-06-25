package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.genes
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.event.CustomEvents
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object GeneEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onGeneChanged(event: CustomEvents.GeneChangeEvent) {
        if (event.isCanceled) return

        val (livingEntity: LivingEntity, gene: Gene, wasAdded: Boolean) = event

        tellAllPlayersGeneChanged(livingEntity, gene, wasAdded)

        if (livingEntity is Player) {
            when (gene) {
                ModGenes.efficiency -> AttributeGenes.setEfficiency(livingEntity, 1, wasAdded)
                ModGenes.efficiencyFour -> AttributeGenes.setEfficiency(livingEntity, 4, wasAdded)

                ModGenes.stepAssist -> AttributeGenes.setStepAssist(livingEntity, wasAdded)
                ModGenes.wallClimbing -> AttributeGenes.setWallClimbing(livingEntity, wasAdded)
                ModGenes.knockback -> AttributeGenes.setKnockback(livingEntity, wasAdded)
                ModGenes.flight -> AttributeGenes.setFlight(livingEntity, wasAdded)

                ModGenes.moreHearts -> AttributeGenes.setMoreHearts(livingEntity, 1, wasAdded)
                ModGenes.moreHeartsTwo -> AttributeGenes.setMoreHearts(livingEntity, 2, wasAdded)
            }
        }

        if (!wasAdded && gene.getPotion() != null) {
            TickGenes.handlePotionGeneRemoved(livingEntity, gene)
        }

        if (livingEntity is ServerPlayer) {
//            AdvancementTriggers.geneAdvancements(livingEntity, gene, wasAdded)
        }

        ModScheduler.scheduleTaskInTicks(1) {
            checkForMissingRequirements(livingEntity)
        }
    }

    private fun checkForMissingRequirements(entity: LivingEntity) {

        val genes = entity.genes

        val genesWithMissingRequirements = genes.filter { gene ->
            !gene.getRequiredGenes().all { it in genes }
        }

        genesWithMissingRequirements.forEach { gene ->
            entity.removeGene(gene)

            val requiredGenesComponent =
                Component.translatable(ModLanguageProvider.Messages.MISSING_GENE_REQUIREMENTS_LIST)

            for (requiredGene in gene.getRequiredGenes()) {
                val hasGene = genes.contains(requiredGene)
                if (hasGene) continue

                requiredGenesComponent.append(Component.literal("\n - ").append(requiredGene.nameComponent))
            }

            entity.sendSystemMessage(
                Component.translatable(
                    ModLanguageProvider.Messages.MISSING_GENE_REQUIREMENTS,
                    gene.nameComponent
                ).withStyle { it.withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, requiredGenesComponent)) }
            )
        }
    }

    private fun tellAllPlayersGeneChanged(entity: LivingEntity, changedGene: Gene, wasAdded: Boolean) {
        if (entity.level().isClientSide) return

        val server = entity.server
        if (server == null) {
            GeneticsResequenced.LOGGER.error("Server is null when trying to tell all players about gene change")
            return
        }

        ModPacketHandler.messageAllPlayers(
            GeneChangedPacket(
                entity.id,
                changedGene,
                wasAdded
            )
        )
    }

}