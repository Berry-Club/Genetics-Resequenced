package dev.aaronhowser.mods.geneticsresequenced.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack

data class SyringeGenesPredicate(
	val genes: List<ResourceKey<Gene>>,
	val isAntigene: Boolean
) : ItemPredicate() {

	override fun matches(stack: ItemStack): Boolean {
		val stackGenes = if (isAntigene) {
			SyringeItem.getAntigenes(stack)
		} else {
			SyringeItem.getGeneRks(stack)
		}

		return genes.all(stackGenes::contains)
	}

	companion object {

		fun blackDeath(): SyringeGenesPredicate {
			return SyringeGenesPredicate(
				listOf(ModGenes.BLACK_DEATH),
				false
			)
		}

		val CODEC: Codec<SyringeGenesPredicate> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
						.listOf()
						.fieldOf("genes")
						.forGetter(SyringeGenesPredicate::genes),
					Codec.BOOL
						.optionalFieldOf("is_antigene", false)
						.forGetter(SyringeGenesPredicate::isAntigene)
				).apply(instance, ::SyringeGenesPredicate)
			}
	}

}