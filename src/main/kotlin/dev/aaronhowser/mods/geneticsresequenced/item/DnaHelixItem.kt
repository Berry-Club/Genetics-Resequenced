package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class DnaHelixItem : EntityDnaItem() {

    companion object {

        fun hasGene(itemStack: ItemStack): Boolean {
            return itemStack.has(GeneItemComponent.component)
        }

        fun getGene(itemStack: ItemStack): Gene? {
            return itemStack.get(GeneItemComponent.component)?.gene
        }

        fun setGene(itemStack: ItemStack, gene: Gene): ItemStack {
            itemStack.set(GeneItemComponent.component, GeneItemComponent(gene))
            return itemStack
        }

        fun setBasic(itemStack: ItemStack): ItemStack {
            return setGene(itemStack, ModGenes.basic)
        }

        fun getAllHelices(): List<ItemStack> {
            val geneRegistry = Gene.getRegistry()
            return geneRegistry.map { gene -> ModItems.DNA_HELIX.toStack().apply { setGene(this, gene) } }
        }
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {
        val gene = getGene(pStack)

        if (gene == null) {
            showNoGeneTooltips(pStack, pTooltipComponents)
        } else {
            pTooltipComponents.add(
                Component.translatable(
                    ModLanguageProvider.Tooltips.GENE,
                    gene.nameComponent
                ).withColor(ChatFormatting.GRAY)
            )
        }

    }

    private fun showNoGeneTooltips(
        pStack: ItemStack,
        pTooltipComponents: MutableList<Component>
    ) {

        pTooltipComponents.add(
            Component.translatable(
                ModLanguageProvider.Tooltips.GENE,
                Gene.unknownGeneComponent
            ).withColor(ChatFormatting.GRAY)
        )

        val entity = getEntityType(pStack)
        if (entity != null) {
            pTooltipComponents.add(
                Component.translatable(
                    ModLanguageProvider.Tooltips.HELIX_ENTITY,
                    entity.description
                ).withColor(ChatFormatting.GRAY)
            )
        }

        try {
            val isCreative = ClientUtil.playerIsCreative()

            if (isCreative) {
                val component =
                    Component.translatable(ModLanguageProvider.Tooltips.CELL_CREATIVE).withColor(ChatFormatting.GRAY)
                pTooltipComponents.add(component)
            }
        } catch (e: Exception) {
            GeneticsResequenced.LOGGER.error("DnaHelixItem isCreative check failed", e)
        }

    }

}