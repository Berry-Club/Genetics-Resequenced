package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import net.minecraft.world.damagesource.DamageSource
import net.minecraftforge.event.entity.living.LivingDamageEvent

object OtherGenes {

    fun handleNoFallDamage(event: LivingDamageEvent) {
        if (event.source != DamageSource.FALL) return

        val genes = event.entity.getGenes()
        if (genes?.hasGene(EnumGenes.NO_FALL_DAMAGE) != true) return

        event.isCanceled = true
    }

}