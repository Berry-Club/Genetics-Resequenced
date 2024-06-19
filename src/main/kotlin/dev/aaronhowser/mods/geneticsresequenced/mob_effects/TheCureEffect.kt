package dev.aaronhowser.mods.geneticsresequenced.mob_effects

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class TheCureEffect : MobEffect(
    MobEffectCategory.BENEFICIAL,
    0xa83283
) {

    override fun isInstantenous(): Boolean = true
    override fun isDurationEffectTick(pDuration: Int, pAmplifier: Int): Boolean = true

    private fun tick(pLivingEntity: LivingEntity) {
        removeAllNegativeGenes(pLivingEntity)
        removeAllNegativeEffects(pLivingEntity)

        pLivingEntity.removeEffect(this)
    }

    private fun removeAllNegativeGenes(pLivingEntity: LivingEntity) {
        val genes = pLivingEntity.getGenes() ?: return

        val genesList = genes.getGeneList()
        genes.removeGenes(pLivingEntity, genesList.filter { it.isNegative })

    }

    private fun removeAllNegativeEffects(pLivingEntity: LivingEntity) {
        val harmfulEffects = pLivingEntity.activeEffects.filter { it.effect.category == MobEffectCategory.HARMFUL }
        for (effect in harmfulEffects) {
            pLivingEntity.removeEffect(effect.effect)
        }
    }

    override fun applyEffectTick(pLivingEntity: LivingEntity, pAmplifier: Int) {
        tick(pLivingEntity)
    }

    override fun applyInstantenousEffect(
        pSource: Entity?,
        pIndirectSource: Entity?,
        pLivingEntity: LivingEntity,
        pAmplifier: Int,
        pHealth: Double
    ) {
        tick(pLivingEntity)
    }

}