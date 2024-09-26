package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class AntiPlasmidItem : Item(Properties().stacksTo(1)) {

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {

        val geneHolder = PlasmidItem.getGene(pStack)

        if (geneHolder == null) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.ANTI_PLASMID_EMPTY
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            )
        } else {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.PLASMID_GENE
                    .toComponent(Gene.getNameComponent(geneHolder))
                    .withColor(ChatFormatting.GRAY)
            )
        }

    }

}