package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.PlasmidProgressItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class PlasmidItem : Item(Properties()) {

    companion object {

        fun getGene(itemStack: ItemStack): Gene? {
            return itemStack.get(PlasmidProgressItemComponent.component)?.gene
        }

        fun setGene(itemStack: ItemStack, gene: Gene, amount: Int = 0) {
            val component = PlasmidProgressItemComponent(
                gene,
                amount
            )
            itemStack.set(PlasmidProgressItemComponent.component, component)
        }

        fun getAmount(itemStack: ItemStack): Int {
            return itemStack.get(PlasmidProgressItemComponent.component)?.dnaPoints ?: 0
        }

        fun setAmount(itemStack: ItemStack, amount: Int) {
            val component = PlasmidProgressItemComponent(
                getGene(itemStack) ?: return,
                amount
            )
            itemStack.set(PlasmidProgressItemComponent.component, component)
        }

        fun increaseAmount(itemStack: ItemStack, amount: Int = 1) {
            setAmount(itemStack, getAmount(itemStack) + amount)
        }

        fun isComplete(itemStack: ItemStack): Boolean {
            val gene = getGene(itemStack) ?: return false
            return getAmount(itemStack) >= gene.dnaPointsRequired
        }

        fun getCompletedPlasmid(gene: Gene): ItemStack {
            return ModItems.PLASMID.toStack().apply {
                setGene(this, gene, gene.dnaPointsRequired)
            }
        }

        fun getAllPlasmids(): List<ItemStack> {
            return Gene.Registry.getRegistry().filter { !it.isHidden }.map { getCompletedPlasmid(it) }
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
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.PLASMID_EMPTY
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            )
            return
        }

        pTooltipComponents.add(
            ModLanguageProvider.Tooltips.PLASMID_GENE
                .toComponent(gene.nameComponent)
                .withColor(ChatFormatting.GRAY)
        )

        if (isComplete(pStack)) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.PLASMID_COMPLETE
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            )
        } else {
            val amountNeeded = gene.dnaPointsRequired
            val amount = getAmount(pStack)

            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.PLASMID_PROGRESS
                    .toComponent(amount, amountNeeded)
                    .withColor(ChatFormatting.GRAY)
            )
        }
    }

    //TODO: Fill item category (probably done in events)

    //TODO: allowedIn (is this even needed still)

}