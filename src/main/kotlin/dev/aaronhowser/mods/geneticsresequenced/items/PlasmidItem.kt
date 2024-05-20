package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class PlasmidItem : Item(Properties().tab(ModItems.MOD_TAB)) {

    companion object {
        private const val AMOUNT_NBT = "Amount"

        private fun ItemStack.getAmount(): Int {
            if (!this.`is`(ModItems.PLASMID.get())) throw IllegalArgumentException("ItemStack is not a PlasmidItem")
            return this.tag?.getInt(AMOUNT_NBT) ?: 0
        }

        fun ItemStack.setAmount(amount: Int): ItemStack {
            if (!this.`is`(ModItems.PLASMID.get())) throw IllegalArgumentException("ItemStack is not a PlasmidItem")
            val tag = this.orCreateTag
            tag.putInt(AMOUNT_NBT, amount)
            return this
        }

        fun ItemStack.increaseAmount(amount: Int = 1): ItemStack {
            if (!this.`is`(ModItems.PLASMID.get())) throw IllegalArgumentException("ItemStack is not a PlasmidItem")
            this.setAmount(getAmount() + amount)
            return this
        }

        fun isComplete(itemStack: ItemStack): Boolean {
            if (!itemStack.`is`(ModItems.PLASMID.get())) throw IllegalArgumentException("ItemStack is not a PlasmidItem")
            val gene = itemStack.getGene() ?: return false
            return itemStack.getAmount() >= gene.dnaPointsRequired
        }

        private fun getAllPlasmids(): List<ItemStack> {
            return Gene.getRegistry().map { gene ->
                val stack =
                    ItemStack(ModItems.PLASMID.get())
                        .setGene(gene)
                        .setAmount(gene.dnaPointsRequired)
                stack
            }
        }
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        val gene = pStack.getGene()

        if (gene == null) {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.plasmid.empty")
                    .withColor(ChatFormatting.GRAY)
            )
            return
        }

        val amountNeeded = gene.dnaPointsRequired
        val amount = pStack.getAmount()

        pTooltipComponents.add(
            Component
                .translatable("tooltip.geneticsresequenced.plasmid.gene")
                .withColor(ChatFormatting.GRAY)
                .append(gene.nameComponent)
        )

        if (isComplete(pStack)) {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.plasmid.complete")
                    .withColor(ChatFormatting.GRAY)
            )
        } else {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.plasmid.amount", amount, amountNeeded)
                    .withColor(ChatFormatting.GRAY)
            )
        }
    }

    override fun fillItemCategory(pCategory: CreativeModeTab, pItems: NonNullList<ItemStack>) {
        if (pCategory == ModItems.MOD_TAB) {
            pItems.add(ItemStack(this))
            pItems.addAll(getAllPlasmids())
        }
    }

    override fun allowedIn(pCategory: CreativeModeTab): Boolean {
        return pCategory == ModItems.MOD_TAB
    }

}