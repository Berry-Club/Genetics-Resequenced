package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class GmoCell : Item(Properties()) {

    companion object {

        fun setDetails(
            itemStack: ItemStack,
            entityType: EntityType<*>,
            geneHolder: Holder<Gene>,
        ) {
            EntityDnaItem.setEntityType(itemStack, entityType)
            DnaHelixItem.setGeneHolder(itemStack, geneHolder)
        }

    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {

        val entityType = EntityDnaItem.getEntityType(pStack)
        if (entityType != null) {
            val entityComponent =
                ModLanguageProvider.Tooltips.CELL_MOB
                    .toComponent(entityType.description)
                    .withStyle(ChatFormatting.GRAY)
            pTooltipComponents.add(entityComponent)
        } else {
            val noEntityComponent =
                ModLanguageProvider.Tooltips.CELL_NO_MOB
                    .toComponent()
                    .withStyle(ChatFormatting.GRAY)
            pTooltipComponents.add(noEntityComponent)
        }

        val geneHolder = DnaHelixItem.getGeneHolder(pStack)
        if (geneHolder != null) {
            val geneComponent =
                ModLanguageProvider.Tooltips.GENE
                    .toComponent(Gene.getNameComponent(geneHolder))
                    .withStyle(ChatFormatting.GRAY)
            pTooltipComponents.add(geneComponent)
        } else {
            val noGeneComponent =
                ModLanguageProvider.Tooltips.GENE
                    .toComponent(Gene.unknownGeneComponent)
                    .withStyle(ChatFormatting.GRAY)
            pTooltipComponents.add(noGeneComponent)
        }

    }

}