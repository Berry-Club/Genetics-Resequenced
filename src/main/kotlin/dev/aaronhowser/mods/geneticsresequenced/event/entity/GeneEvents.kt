package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.genes
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.event.CustomEvents
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
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
    fun onGeneChanged(event: CustomEvents.GeneChangeEvent.Post) {
        val (livingEntity: LivingEntity, gene: Gene, wasAdded: Boolean) = event

        tellAllPlayersGeneChanged(livingEntity, gene, wasAdded)

        if (livingEntity is Player) {
            when (gene) {
                ModGenes.EFFICIENCY.get() -> AttributeGenes.setEfficiency(livingEntity, 1, wasAdded)
                ModGenes.EFFICIENCY_FOUR.get() -> AttributeGenes.setEfficiency(livingEntity, 4, wasAdded)

                ModGenes.STEP_ASSIST.get() -> AttributeGenes.setStepAssist(livingEntity, wasAdded)
                ModGenes.WALL_CLIMBING.get() -> AttributeGenes.setWallClimbing(livingEntity, wasAdded)
                ModGenes.KNOCKBACK.get() -> AttributeGenes.setKnockback(livingEntity, wasAdded)
                ModGenes.FLIGHT.get() -> AttributeGenes.setFlight(livingEntity, wasAdded)

                ModGenes.MORE_HEARTS.get() -> AttributeGenes.setMoreHearts(livingEntity, 1, wasAdded)
                ModGenes.MORE_HEARTS_TWO.get() -> AttributeGenes.setMoreHearts(livingEntity, 2, wasAdded)
            }
        }

        if (!wasAdded && gene.getPotion() != null) {
            TickGenes.handlePotionGeneRemoved(livingEntity, gene)
        }

        if (livingEntity is ServerPlayer) {
            AdvancementTriggers.geneAdvancements(livingEntity, gene, wasAdded)
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
                ModLanguageProvider.Messages.MISSING_GENE_REQUIREMENTS_LIST.toComponent()

            for (requiredGene in gene.getRequiredGenes()) {
                val hasGene = genes.contains(requiredGene)
                if (hasGene) continue

                requiredGenesComponent.append(Component.literal("\n - ").append(requiredGene.nameComponent))
            }
            if (!entity.level().isClientSide) {
                entity.sendSystemMessage(
                    ModLanguageProvider.Messages.MISSING_GENE_REQUIREMENTS
                        .toComponent(gene.nameComponent)
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
                changedGene.id,
                wasAdded
            )
        )
    }

}