package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class GmoItem : Item(Properties().tab(ModItems.MOD_TAB)) {

    companion object {

        fun setDetails(
            itemStack: ItemStack,
            entityType: EntityType<*>,
            gene: Gene,
        ) {
            itemStack.setGene(gene)
            setFailureGene(itemStack, gene)
            itemStack.setMob(entityType)
        }

        private const val FAILURE_GENE_NBT = "FailureGene"

        private fun setFailureGene(itemStack: ItemStack, failureGene: Gene): ItemStack {
            val stack = itemStack.copy()
            val tag = stack.orCreateTag
            tag.putString(FAILURE_GENE_NBT, failureGene.id.toString())
            return stack
        }

        fun getFailureGene(itemStack: ItemStack): Gene? {
            val string = itemStack.tag?.getString(FAILURE_GENE_NBT)
            if (string.isNullOrBlank()) return null
            return Gene.fromId(string)
        }

    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {

        val entityType = EntityDnaItem.getEntityType(pStack)
        if (entityType != null) {
            val component =
                Component
                    .translatable("tooltip.geneticsresequenced.dna_item.filled", entityType.description)
                    .withColor(ChatFormatting.GRAY)

            pTooltipComponents.add(component)
        } else {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.dna_item.empty")
                    .withColor(ChatFormatting.GRAY)
            )
        }

        val gene = pStack.getGene()
        if (gene != null) {
            val geneComponent =
                Component
                    .translatable("tooltip.geneticsresequenced.gene")
                    .append(gene.nameComponent)
                    .withColor(ChatFormatting.GRAY)

            pTooltipComponents.add(geneComponent)
        } else {
            val noGeneComponent =
                Component.translatable(
                    "tooltip.geneticsresequenced.gene",
                    Gene.unknownGeneComponent
                ).withColor(ChatFormatting.GRAY)

            pTooltipComponents.add(noGeneComponent)
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    }

}