package dev.aaronhowser.mods.geneticsresequenced.event.entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DamageGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DeathGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.ScareGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Monster
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
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
//        DeathGenes.handleSlimyDeath(event)
    }

    @SubscribeEvent
    fun onDetonate(event: ExplosionEvent.Detonate) {
        DeathGenes.explosiveExitDetonation(event)
    }

    @SubscribeEvent
    fun onLivingDamagePre(event: LivingDamageEvent.Pre) {
        DamageGenes.handleNoFallDamage(event)
        DamageGenes.handleWitherProof(event)
        DamageGenes.handleFireProof(event)
        DamageGenes.handlePoisonProof(event)

        DamageGenes.handleDragonHealth(event) //must be last
    }

    @SubscribeEvent
    fun onLivingHurt(event: LivingDamageEvent.Post) {
        DamageGenes.handleThorns(event)
        DamageGenes.handleClaws(event)
        DamageGenes.handleWitherHit(event)
        DamageGenes.handleJohnny(event)
        DamageGenes.handleChilling(event)
    }

    //TODO: Make sure this works with players and not just entities
    @SubscribeEvent
    fun onLivingTick(event: EntityTickEvent.Pre) {
        val entity = event.entity as? LivingEntity ?: return

        TickGenes.handleBioluminescence(entity)
        TickGenes.handlePhotosynthesis(entity)
        TickGenes.handleTickingGenes(entity)
    }

    @SubscribeEvent
    fun onEntitySpawn(event: EntityJoinLevelEvent) {
        val entity = event.entity

        if (entity is Monster) {
            ScareGenes.attachScareTask(entity)
        }
    }

}