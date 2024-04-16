package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.setGene
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

object PlasmidItem : Item(Properties().tab(ModItems.MOD_TAB)) {

    private const val AMOUNT_NBT = "Amount"

    private fun ItemStack.getAmount(): Int {
        if (!this.`is`(PlasmidItem)) throw IllegalArgumentException("ItemStack is not a PlasmidItem")
        return this.tag?.getInt(AMOUNT_NBT) ?: 0
    }

    fun ItemStack.setAmount(amount: Int): ItemStack {
        if (!this.`is`(PlasmidItem)) throw IllegalArgumentException("ItemStack is not a PlasmidItem")
        val tag = this.orCreateTag
        tag.putInt(AMOUNT_NBT, amount)
        return this
    }

    fun ItemStack.increaseAmount(amount: Int = 1): ItemStack {
        if (!this.`is`(PlasmidItem)) throw IllegalArgumentException("ItemStack is not a PlasmidItem")
        this.setAmount(getAmount() + amount)
        return this
    }

    fun isComplete(itemStack: ItemStack): Boolean {
        if (!itemStack.`is`(PlasmidItem)) throw IllegalArgumentException("ItemStack is not a PlasmidItem")
        val gene = itemStack.getGene() ?: return false
        return itemStack.getAmount() >= gene.amountNeeded
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
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
            return
        }

        val amountNeeded = gene.amountNeeded
        val amount = pStack.getAmount()

        pTooltipComponents.add(
            Component
                .translatable("tooltip.geneticsresequenced.plasmid.gene")
                .withStyle { it.withColor(ChatFormatting.GRAY) }
                .append(gene.nameComponent)
        )

        if (isComplete(pStack)) {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.plasmid.complete")
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
        } else {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.plasmid.amount", amount, amountNeeded)
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
        }
    }

    private fun getAllPlasmids(): List<ItemStack> {
        return Gene.getRegistry().map { gene ->
            val stack =
                ItemStack(PlasmidItem)
                    .setGene(gene)
                    .setAmount(gene.amountNeeded)
            stack
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