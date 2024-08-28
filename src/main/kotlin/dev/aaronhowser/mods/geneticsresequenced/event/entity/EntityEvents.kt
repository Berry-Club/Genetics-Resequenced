package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DamageGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DeathGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.ScareGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
import net.neoforged.neoforge.event.level.ExplosionEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object EntityEvents {

    @SubscribeEvent
    fun onLivingDeath(event: LivingDeathEvent) {
        DeathGenes.handleEmeraldHeart(event)
        DeathGenes.handleExplosiveExit(event)
        DeathGenes.handleSlimyDeath(event)
    }

    @SubscribeEvent
    fun onDetonate(event: ExplosionEvent.Detonate) {
        DeathGenes.explosiveExitDetonation(event)
    }

    @SubscribeEvent
    fun onLivingAboutToBeDamaged(event: LivingIncomingDamageEvent) {
        DamageGenes.handleNoFallDamage(event)
        DamageGenes.handleWitherProof(event)
        DamageGenes.handleFireProof(event)
        DamageGenes.handlePoisonProof(event)
    }

    @SubscribeEvent
    fun onLivingDamagePre(event: LivingDamageEvent.Pre) {
        DamageGenes.handleDragonHealth(event)
        DamageGenes.handleJohnny(event)
    }

    @SubscribeEvent
    fun onLivingHurtPost(event: LivingDamageEvent.Post) {
        if (event.newDamage <= 0f) return

        DamageGenes.handleThorns(event)
        DamageGenes.handleClaws(event)
        DamageGenes.handleWitherHit(event)
        DamageGenes.handleChilling(event)
    }

    @SubscribeEvent
    fun onEntityTick(event: EntityTickEvent.Pre) {
        val entity = event.entity as? LivingEntity ?: return

        TickGenes.handleBioluminescence(entity)
        TickGenes.handlePhotosynthesis(entity)
        TickGenes.handleTickingGenes(entity)
    }

    @SubscribeEvent
    fun onEntitySpawn(event: EntityJoinLevelEvent) {
        val entity = event.entity
        if (entity is PathfinderMob) {
            ScareGenes.attachScareTask(entity)
        }
    }

}