package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneRkItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class DnaHelixItem : EntityDnaItem() {

    companion object {

        fun hasGene(itemStack: ItemStack): Boolean {
            return itemStack.has(ModDataComponents.GENE_COMPONENT)
        }

        fun getGene(itemStack: ItemStack, registries: HolderLookup.Provider): Holder<Gene>? {
            return getGeneRk(itemStack)?.getHolder(registries)
        }

        fun getGeneRk(itemStack: ItemStack): ResourceKey<Gene>? {
            return itemStack.get(ModDataComponents.GENE_COMPONENT)?.geneRk
        }

        fun setGeneRk(itemStack: ItemStack, geneHolder: ResourceKey<Gene>): ItemStack {
            itemStack.set(ModDataComponents.GENE_COMPONENT, GeneRkItemComponent(geneHolder))
            return itemStack
        }

        fun setBasic(itemStack: ItemStack): ItemStack {
            return setGeneRk(itemStack, ModGenes.BASIC)
        }

        fun getAllHelices(registries: HolderLookup.Provider): List<ItemStack> {
            return GeneRegistry.getRegistrySorted(registries)
                .map { gene -> ModItems.DNA_HELIX.toStack().apply { setGeneRk(this, gene.key!!) } }
        }
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {
        val geneHolder = getGene(pStack, ClientUtil.localRegistryAccess!!)

        if (geneHolder == null) {
            showNoGeneTooltips(pStack, pTooltipComponents)
        } else {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.GENE
                    .toComponent(geneHolder.value().nameComponent(ClientUtil.localRegistryAccess!!))
                    .withColor(ChatFormatting.GRAY)
            )
        }

    }

    private fun showNoGeneTooltips(
        pStack: ItemStack,
        pTooltipComponents: MutableList<Component>
    ) {

        pTooltipComponents.add(
            ModLanguageProvider.Tooltips.GENE
                .toComponent(Gene.unknownGeneComponent)
                .withColor(ChatFormatting.GRAY)
        )

        val entity = getEntityType(pStack)
        if (entity != null) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.HELIX_ENTITY
                    .toComponent(entity.description)
                    .withColor(ChatFormatting.GRAY)
            )
        }

        try {
            val isCreative = ClientUtil.playerIsCreative()

            if (isCreative) {
                val component =
                    ModLanguageProvider.Tooltips.CELL_CREATIVE
                        .toComponent()
                        .withColor(ChatFormatting.GRAY)
                pTooltipComponents.add(component)
            }
        } catch (e: Exception) {
            GeneticsResequenced.LOGGER.error("DnaHelixItem isCreative check failed", e)
        }

    }

}