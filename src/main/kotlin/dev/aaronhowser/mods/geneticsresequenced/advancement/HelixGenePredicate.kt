package dev.aaronhowser.mods.geneticsresequenced.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.advancements.critereon.ItemSubPredicate
import net.minecraft.advancements.critereon.SingleComponentItemPredicate
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.ItemStack
import java.util.*

/**
 * Predicate for checking if an item has a specific gene.
 *
 * An empty optional means that it will return true if the stack has any gene.
 */
data class HelixGenePredicate(
    val gene: Optional<Holder<Gene>>
) : SingleComponentItemPredicate<Holder<Gene>> {

    override fun matches(stack: ItemStack, requiredGene: Holder<Gene>): Boolean {
        if (gene.isEmpty) return DnaHelixItem.hasGene(stack)

        val stackGene = DnaHelixItem.getGeneHolder(stack) ?: return false
        return stackGene.isGene(requiredGene)
    }

    override fun componentType(): DataComponentType<Holder<Gene>> {
        return ModDataComponents.GENE_COMPONENT.get()
    }

    companion object {
        fun any() = HelixGenePredicate(Optional.empty())


        fun blackDeath(lookup: HolderLookup.Provider): HelixGenePredicate {
            val blackDeathGeneHolder = ModGenes.BLACK_DEATH.getHolderOrThrow(lookup)

            return HelixGenePredicate(
                Optional.of(blackDeathGeneHolder)
            )
        }

        val CODEC: Codec<HelixGenePredicate> =
            RecordCodecBuilder.create { instance ->
                instance.group(
                    Gene.CODEC
                        .optionalFieldOf("gene")
                        .forGetter(HelixGenePredicate::gene),
                ).apply(instance, ::HelixGenePredicate)
            }

        val TYPE: ItemSubPredicate.Type<HelixGenePredicate> = ItemSubPredicate.Type(CODEC)
    }

}