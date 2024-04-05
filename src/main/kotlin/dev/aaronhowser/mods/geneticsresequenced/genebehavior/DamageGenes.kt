package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.IndirectEntityDamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDamageEvent

object DamageGenes {

    fun handleNoFallDamage(event: LivingDamageEvent) {
        if (event.source != DamageSource.FALL) return

        val genes = event.entity.getGenes()
        if (genes?.hasGene(EnumGenes.NO_FALL_DAMAGE) != true) return

        event.isCanceled = true
    }

    fun handleWitherProof(event: LivingDamageEvent) {
        if (event.source != DamageSource.WITHER) return

        val genes = event.entity.getGenes()
        if (genes?.hasGene(EnumGenes.WITHER_PROOF) != true) return

        event.entity.removeEffect(MobEffects.WITHER)
        event.isCanceled = true
    }

    fun handleWitherHit(event: LivingAttackEvent) {
        // Makes it not proc if it's an arrow or whatever
        if (event.source is IndirectEntityDamageSource) return

        val witherEffect = MobEffectInstance(
            MobEffects.WITHER,
            100
        )

        if (!event.entity.canBeAffected(witherEffect)) return

        val attacker = event.source.entity as? LivingEntity ?: return
        val genes = attacker.getGenes()
        if (genes?.hasGene(EnumGenes.WITHER_HIT) != true) return

        event.entity.addEffect(witherEffect)
    }

    fun handleFireProof(event: LivingDamageEvent) {
        val source = event.source

        if (!source.isFire) return

        val genes = event.entity.getGenes()
        if (genes?.hasGene(EnumGenes.FIRE_PROOF) != true) return

        event.entity.clearFire()
        event.isCanceled = true
    }

    fun handlePoisonProof(event: LivingDamageEvent) {
        val source = event.source

        if (!source.isMagic) return

        val genes = event.entity.getGenes()
        if (genes?.hasGene(EnumGenes.POISON_IMMUNITY) != true) return

        event.entity.removeEffect(MobEffects.POISON)
        event.isCanceled = true
    }

}