package dev.aaronhowser.mods.genetics_resequenced.effect

import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.genetics_resequenced.attachment.TemporaryGenesData.Companion.temporaryGeneHolders
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.genetics_resequenced.registry.ModEffects
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class PanaceaEffect : MobEffect(
	MobEffectCategory.BENEFICIAL,
	0xa83283
) {

	override fun isInstantenous(): Boolean = true

	override fun applyInstantenousEffect(
		pSource: Entity?,
		pIndirectSource: Entity?,
		pLivingEntity: LivingEntity,
		pAmplifier: Int,
		pHealth: Double
	) {
		removeAllNegativeGenes(pLivingEntity)
		removeAllNegativeEffects(pLivingEntity)

		pLivingEntity.removeEffect(ModEffects.PANACEA)
	}

	companion object {

		private fun removeAllNegativeGenes(livingEntity: LivingEntity) {
			val genes = livingEntity.permanentGeneHolders.filter { it.isNegative }.iterator()

			while (genes.hasNext()) {
				val gene = genes.next()
				livingEntity.removeGene(gene)
			}

			val tempGenes = livingEntity.temporaryGeneHolders.filter { it.isNegative }.iterator()

			while (tempGenes.hasNext()) {
				val gene = tempGenes.next()
				livingEntity.removeGene(gene)
			}
		}

		private fun removeAllNegativeEffects(livingEntity: LivingEntity) {
			val harmfulEffects = livingEntity.activeEffects
				.filter { it.effect.value().category == MobEffectCategory.HARMFUL }
				.iterator()

			while (harmfulEffects.hasNext()) {
				val effect = harmfulEffects.next()
				livingEntity.removeEffect(effect.effect)
			}
		}
	}

}