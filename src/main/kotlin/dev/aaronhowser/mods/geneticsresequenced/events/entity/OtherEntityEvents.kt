package dev.aaronhowser.mods.geneticsresequenced.events.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.advancements.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.api.capability.CapabilityHandler
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.events.CustomEvents
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.DamageGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.DeathGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.level.ExplosionEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherEntityEvents {

    @SubscribeEvent
    fun onAttachCapability(event: AttachCapabilitiesEvent<Entity>) {
        val entity = event.`object`

        if (entity !is LivingEntity) return

        event.addCapability(CapabilityHandler.GENE_CAPABILITY_RL, GenesCapabilityProvider())
    }

    @SubscribeEvent
    fun onLivingDamage(event: LivingDamageEvent) {
        DamageGenes.handleNoFallDamage(event)
        DamageGenes.handleWitherProof(event)
        DamageGenes.handleFireProof(event)
        DamageGenes.handlePoisonProof(event)

        DamageGenes.handleDragonHealth(event) //must be last
    }

    @SubscribeEvent
    fun onLivingHurt(event: LivingHurtEvent) {
        DamageGenes.handleThorns(event)
        DamageGenes.handleClaws(event)
        DamageGenes.handleWitherHit(event)
        DamageGenes.handleJohnny(event)
        DamageGenes.handleChilling(event)
    }

    @SubscribeEvent
    fun onLivingDeath(event: LivingDeathEvent) {
        DeathGenes.handleEmeraldHeart(event)
        DeathGenes.handleExplosiveExit(event)
        DeathGenes.handleSlimyDeath(event)
    }

    @SubscribeEvent
    fun onLivingTick(event: LivingTickEvent) {
        val entity = event.entity

        TickGenes.handleBioluminescence(entity)
        TickGenes.handlePhotosynthesis(entity)
        TickGenes.handleTickingGenes(entity)
    }

    @SubscribeEvent
    fun onDetonate(event: ExplosionEvent.Detonate) {
        DeathGenes.explosiveExitDetonation(event)
    }

    private fun tellAllPlayersGeneChanged(entity: LivingEntity, changedGene: Gene, wasAdded: Boolean) {
        if (entity.level.isClientSide) return

        val server = entity.server
        if (server == null) {
            GeneticsResequenced.LOGGER.error("Server is null when trying to tell all players about gene change")
            return
        }

        for (player in server.playerList.players) {
            val serverPlayer = player ?: continue

            ModPacketHandler.messagePlayer(
                serverPlayer,
                GeneChangedPacket(
                    entity.id,
                    changedGene.id,
                    wasAdded
                )
            )
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onGeneChanged(event: CustomEvents.GeneChangeEvent) {
        if (event.isCanceled) return

        val (livingEntity: LivingEntity, gene: Gene, wasAdded: Boolean) = event

        tellAllPlayersGeneChanged(livingEntity, gene, wasAdded)

        if (livingEntity is Player) {
            when (gene) {
                ModGenes.efficiency -> {
                    if (!livingEntity.hasGene(ModGenes.efficiencyFour)) {
                        val levelToSetTo = if (wasAdded) 1 else 0
                        AttributeGenes.setEfficiency(livingEntity, levelToSetTo)
                    }
                }

                ModGenes.efficiencyFour -> {
                    if (wasAdded) {
                        AttributeGenes.setEfficiency(livingEntity, 4)
                    } else {
                        val levelToSetTo = if (livingEntity.hasGene(ModGenes.efficiency)) 1 else 0
                        AttributeGenes.setEfficiency(livingEntity, levelToSetTo)
                    }
                }

                ModGenes.stepAssist -> AttributeGenes.setStepAssist(livingEntity, wasAdded)

                ModGenes.wallClimbing -> AttributeGenes.setWallClimbing(livingEntity, wasAdded)

                ModGenes.knockback -> AttributeGenes.setKnockback(livingEntity, wasAdded)

                ModGenes.moreHearts -> {
                    AttributeGenes.setMoreHearts(livingEntity, 1, wasAdded)
                }

                ModGenes.moreHeartsTwo -> {
                    AttributeGenes.setMoreHearts(livingEntity, 2, wasAdded)
                }

                ModGenes.flight -> TickGenes.handleFlight(livingEntity)

                else -> {}
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
        val entityGenes = entity.getGenes() ?: return

        val newGenes = entityGenes.getGeneList()

        val genesWithMissingRequirements = newGenes.filter { gene ->
            !gene.getRequiredGenes().all { it in newGenes }
        }

        genesWithMissingRequirements.forEach { gene ->
            entityGenes.removeGene(entity, gene)

            val requiredGenesComponent =
                Component.translatable("message.geneticsresequenced.gene_missing_requirements.list")

            for (requiredGene in gene.getRequiredGenes()) {
                val hasGene = newGenes.contains(requiredGene)
                if (hasGene) continue

                requiredGenesComponent.append(Component.literal("\n - ").append(requiredGene.nameComponent))
            }

            entity.sendSystemMessage(
                Component.translatable(
                    "message.geneticsresequenced.gene_missing_requirements",
                    gene.nameComponent
                ).withStyle { it.withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, requiredGenesComponent)) }
            )
        }
    }

}