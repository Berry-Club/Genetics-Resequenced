package dev.aaronhowser.mods.geneticsresequenced.events.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.CapabilityHandler
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.DamageGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.DeathGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.TickGenes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
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

}