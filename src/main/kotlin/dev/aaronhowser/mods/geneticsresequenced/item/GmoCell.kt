package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
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
            gene: Gene,
        ) {

            EntityDnaItem.setMob(itemStack, entityType)
            DnaHelixItem.setGene(itemStack, gene)
        }

        //TODO: getAllGmoCells

    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {

        val entityType = EntityDnaItem.getEntityType(pStack)
        if (entityType != null) {
            val entityComponent = Component
                .translatable(ModLanguageProvider.Tooltips.CELL_MOB, entityType.description)
                .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(entityComponent)
        } else {
            val noEntityComponent = Component
                .translatable(ModLanguageProvider.Tooltips.CELL_NO_MOB)
                .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(noEntityComponent)
        }

        val gene = DnaHelixItem.getGene(pStack)
        if (gene != null) {
            val geneComponent =
                Component
                    .translatable(ModLanguageProvider.Tooltips.GENE, gene.nameComponent)
                    .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(geneComponent)
        } else {
            val noGeneComponent = Component
                .translatable(ModLanguageProvider.Tooltips.GENE, Gene.unknownGeneComponent)
                .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(noGeneComponent)
        }

    }

}