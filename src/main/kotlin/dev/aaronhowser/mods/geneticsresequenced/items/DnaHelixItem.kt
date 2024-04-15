package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

object DnaHelixItem : EntityDnaItem() {

    private const val GENE_ID_NBT = "GeneId"

    fun hasGene(itemStack: ItemStack): Boolean {
        return itemStack.tag?.contains(GENE_ID_NBT) ?: false
    }

    fun getGene(itemStack: ItemStack): Gene? {
        val string = itemStack.tag?.getString(GENE_ID_NBT) ?: return null
        return Gene.valueOf(string)
    }

    fun setGene(itemStack: ItemStack, gene: Gene): Boolean {
        val tag = itemStack.orCreateTag
        tag.putString(GENE_ID_NBT, gene.id)

        return true
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        val gene = getGene(pStack)

        if (gene == null) {
            pTooltipComponents.add(Component.literal("Gene: None")
                .withStyle { it.withColor(ChatFormatting.GRAY) })
        } else {
            pTooltipComponents.add(Component.literal("Gene: ${gene.id}")
                .withStyle { it.withColor(ChatFormatting.GRAY) })
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    }

}