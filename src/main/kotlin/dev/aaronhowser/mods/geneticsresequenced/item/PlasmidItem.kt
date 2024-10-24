package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isHidden
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.PlasmidProgressItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class PlasmidItem : Item(Properties().stacksTo(1)) {

    companion object {

        fun hasGene(itemStack: ItemStack): Boolean = itemStack.has(ModDataComponents.PLASMID_PROGRESS_COMPONENT)

        fun getGene(itemStack: ItemStack): Holder<Gene>? {
            return itemStack.get(ModDataComponents.PLASMID_PROGRESS_COMPONENT)?.geneHolder
        }

        fun setGene(itemStack: ItemStack, geneHolder: Holder<Gene>, amount: Int = 0) {
            val component = PlasmidProgressItemComponent(
                geneHolder,
                amount
            )
            itemStack.set(ModDataComponents.PLASMID_PROGRESS_COMPONENT, component)
        }

        fun getDnaPoints(itemStack: ItemStack): Int {
            return itemStack.get(ModDataComponents.PLASMID_PROGRESS_COMPONENT)?.dnaPoints ?: 0
        }

        fun setDnaPoints(itemStack: ItemStack, amount: Int) {
            val component = PlasmidProgressItemComponent(
                getGene(itemStack) ?: return,
                amount
            )
            itemStack.set(ModDataComponents.PLASMID_PROGRESS_COMPONENT, component)
        }

        fun increaseDnaPoints(itemStack: ItemStack, amount: Int = 1) {
            setDnaPoints(itemStack, getDnaPoints(itemStack) + amount)
        }

        fun isComplete(itemStack: ItemStack): Boolean {
            val geneHolder = getGene(itemStack) ?: return false
            return getDnaPoints(itemStack) >= geneHolder.value().dnaPointsRequired
        }

        fun getCompletedPlasmid(geneHolder: Holder<Gene>): ItemStack {
            return ModItems.PLASMID.toStack().apply {
                setGene(this, geneHolder, geneHolder.value().dnaPointsRequired)
            }
        }

        fun getAllPlasmids(registries: HolderLookup.Provider): List<ItemStack> {
            return ModGenes.getRegistrySorted(registries)
                .filter { !it.isHidden }
                .map { getCompletedPlasmid(it) }
        }

    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {
        val geneHolder = getGene(pStack)

        if (geneHolder == null) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.PLASMID_EMPTY
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            )
            return
        }

        pTooltipComponents.add(
            ModLanguageProvider.Tooltips.PLASMID_GENE
                .toComponent(Gene.getNameComponent(geneHolder))
                .withColor(ChatFormatting.GRAY)
        )

        if (isComplete(pStack)) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.PLASMID_COMPLETE
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            )
        } else {
            val amountNeeded = geneHolder.value().dnaPointsRequired
            val amount = getDnaPoints(pStack)

            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.PLASMID_PROGRESS
                    .toComponent(amount, amountNeeded)
                    .withColor(ChatFormatting.GRAY)
            )
        }
    }

}