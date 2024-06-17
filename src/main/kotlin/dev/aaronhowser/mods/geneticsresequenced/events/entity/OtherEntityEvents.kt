package dev.aaronhowser.mods.geneticsresequenced.events.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.CapabilityHandler
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.DamageGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.DeathGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent
import net.minecraftforge.event.level.ExplosionEvent
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
        DamageGenes.handleThorns(event)
        DamageGenes.handleClaws(event)

        DamageGenes.handleDragonHealth(event) //must be last
    }

    @SubscribeEvent
    fun onLivingDeath(event: LivingDeathEvent) {
        DeathGenes.handleEmeraldHeart(event)
        DeathGenes.handleExplosiveExit(event)
        DeathGenes.handleSlimyDeath(event)
    }

    @SubscribeEvent
    fun onAttack(event: LivingAttackEvent) {
        DamageGenes.handleWitherHit(event)
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
        val server = entity.server!!

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

    fun genesChanged(entity: LivingEntity, changedGene: Gene, wasAdded: Boolean) {

        tellAllPlayersGeneChanged(entity, changedGene, wasAdded)

        val entityGenes = entity.getGenes() ?: return

        if (entity is Player) {
            when (changedGene) {
                DefaultGenes.efficiency -> {
                    if (entityGenes.hasGene(DefaultGenes.efficiencyFour)) return
                    val levelToSetTo = if (wasAdded) 1 else 0
                    AttributeGenes.setEfficiency(entity, levelToSetTo)
                }

                DefaultGenes.efficiencyFour -> {
                    if (wasAdded) {
                        AttributeGenes.setEfficiency(entity, 4)
                    } else {
                        val levelToSetTo = if (entityGenes.hasGene(DefaultGenes.efficiency)) 1 else 0
                        AttributeGenes.setEfficiency(entity, levelToSetTo)
                    }
                }

                DefaultGenes.stepAssist -> AttributeGenes.setStepAssist(entity, wasAdded)

                DefaultGenes.wallClimbing -> AttributeGenes.setWallClimbing(entity, wasAdded)

                DefaultGenes.moreHearts -> {
                    AttributeGenes.setMoreHearts(entity, 1, wasAdded)
                }

                DefaultGenes.moreHeartsTwo -> {
                    AttributeGenes.setMoreHearts(entity, 2, wasAdded)
                }

                DefaultGenes.flight -> TickGenes.handleFlight(entity)

                else -> {}
            }
        }

        if (!wasAdded && changedGene.getPotion() != null) {
            TickGenes.handlePotionGeneRemoved(entity, changedGene)
        }

        ModScheduler.scheduleTaskInTicks(1) {
            checkForMissingRequirements(entity)
        }
    }

    private fun checkForMissingRequirements(entity: LivingEntity) {
        val entityGenes = entity.getGenes() ?: return

        val newGenes = entityGenes.getGeneList()

        val genesWithMissingRequirements = newGenes.filter { gene ->
            !gene.getRequiredGenes().all { it in newGenes }
        }

        genesWithMissingRequirements.forEach { gene ->
            entityGenes.removeGene(gene)
            genesChanged(entity, gene, false)

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