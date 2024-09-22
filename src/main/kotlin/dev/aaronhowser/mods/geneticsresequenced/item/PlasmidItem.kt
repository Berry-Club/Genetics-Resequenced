package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isHidden
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.components.PlasmidProgressItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class PlasmidItem : Item(Properties().stacksTo(1)) {

    companion object {

        fun hasGene(itemStack: ItemStack): Boolean = itemStack.has(ModDataComponents.PLASMID_PROGRESS_COMPONENT)

        fun getGeneRk(itemStack: ItemStack): ResourceKey<Gene>? {
            return itemStack.get(ModDataComponents.PLASMID_PROGRESS_COMPONENT)?.geneRk
        }

        fun setGeneRk(itemStack: ItemStack, geneRk: ResourceKey<Gene>, amount: Int = 0) {
            val component = PlasmidProgressItemComponent(
                geneRk,
                amount
            )

            itemStack.set(ModDataComponents.PLASMID_PROGRESS_COMPONENT, component)
        }

        fun getDnaPoints(itemStack: ItemStack): Int {
            return itemStack.get(ModDataComponents.PLASMID_PROGRESS_COMPONENT)?.dnaPoints ?: 0
        }

        fun setDnaPoints(itemStack: ItemStack, amount: Int) {
            val component = PlasmidProgressItemComponent(
                getGeneRk(itemStack) ?: return,
                amount
            )
            itemStack.set(ModDataComponents.PLASMID_PROGRESS_COMPONENT, component)
        }

        fun increaseDnaPoints(itemStack: ItemStack, amount: Int = 1) {
            setDnaPoints(itemStack, getDnaPoints(itemStack) + amount)
        }

        fun isComplete(
            itemStack: ItemStack,
            registries: HolderLookup.Provider
        ): Boolean {
            val geneRk = getGeneRk(itemStack) ?: return false
            val geneHolder = geneRk.getHolder(registries) ?: return false
            return getDnaPoints(itemStack) >= geneHolder.value().dnaPointsRequired
        }

        fun getCompletedPlasmid(
            geneRk: ResourceKey<Gene>,
            registries: HolderLookup.Provider
        ): ItemStack {
            val geneHolder = geneRk.getHolder(registries)!!

            return ModItems.PLASMID.toStack().apply {
                setGeneRk(this, geneRk, geneHolder.value().dnaPointsRequired)
            }
        }

        fun getAllPlasmids(registries: HolderLookup.Provider): List<ItemStack> {
            return GeneRegistry.getRegistrySorted(registries)
                .filter { !it.isHidden && !it.isDisabled }
                .map { getCompletedPlasmid(it.key!!, registries) }
        }

    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {
        val geneRk = getGeneRk(pStack)

        if (geneRk == null) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.PLASMID_EMPTY
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            )

            return
        }

        pTooltipComponents.add(
            ModLanguageProvider.Tooltips.PLASMID_GENE
                .toComponent(Gene.getNameComponent(geneRk))
                .withColor(ChatFormatting.GRAY)
        )

        if (isComplete(pStack, pContext.registries()!!)) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.PLASMID_COMPLETE
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            )
        } else {
            val geneHolder = geneRk.getHolder(pContext.registries()!!)!!
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