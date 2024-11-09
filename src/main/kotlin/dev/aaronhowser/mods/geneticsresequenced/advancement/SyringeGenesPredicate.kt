package dev.aaronhowser.mods.geneticsresequenced.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.advancements.critereon.ItemSubPredicate
import net.minecraft.advancements.critereon.SingleComponentItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack

data class SyringeGenesPredicate(
    val genes: HolderSet<Gene>,
    val isAntigene: Boolean
) : SingleComponentItemPredicate<HolderSet<Gene>> {

    override fun matches(stack: ItemStack, requiredGenes: HolderSet<Gene>): Boolean {
        val stackGenes = if (isAntigene) {
            SyringeItem.getAntigenes(stack)
        } else {
            SyringeItem.getGenes(stack)
        }

        return requiredGenes.all { requiredGene ->
            stackGenes.any { it.isGene(requiredGene) }
        }
    }

    override fun componentType(): DataComponentType<HolderSet<Gene>> {
        return if (isAntigene) {
            ModDataComponents.ANTIGENES_COMPONENT.get()
        } else {
            ModDataComponents.GENES_COMPONENT.get()
        }
    }

    companion object {

        fun blackDeath(lookup: HolderLookup.Provider): SyringeGenesPredicate {
            val blackDeathGeneHolder = ModGenes.BLACK_DEATH.getHolderOrThrow(lookup)

            return SyringeGenesPredicate(
                HolderSet.direct(blackDeathGeneHolder),
                false
            )
        }

        val CODEC: Codec<SyringeGenesPredicate> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Gene.HOLDER_SET_CODEC
                        .fieldOf("genes")
                        .forGetter(SyringeGenesPredicate::genes),
                    Codec.BOOL
                        .optionalFieldOf("is_antigene", false)
                        .forGetter(SyringeGenesPredicate::isAntigene)
                ).apply(instance, ::SyringeGenesPredicate)
            }

        val TYPE: ItemSubPredicate.Type<SyringeGenesPredicate> = ItemSubPredicate.Type(CODEC)
    }

}