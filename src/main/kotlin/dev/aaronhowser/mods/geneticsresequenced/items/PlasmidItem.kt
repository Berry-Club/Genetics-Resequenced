package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

object PlasmidItem : Item(Properties().tab(ModItems.MOD_TAB)) {

    private val GENE_ID_NBT = "GeneId"
    private val AMOUNT_NBT = "Amount"

    fun hasGene(itemStack: ItemStack): Boolean {
        return itemStack.tag?.contains(GENE_ID_NBT) ?: false
    }

    fun getGene(itemStack: ItemStack): Gene? {
        val geneString = itemStack.tag?.getString(GENE_ID_NBT)
        if (geneString.isNullOrBlank()) return null
        return Gene.fromId(geneString)
    }

    fun setGene(itemStack: ItemStack, gene: Gene): Boolean {
        val tag = itemStack.orCreateTag
        tag.putString(GENE_ID_NBT, gene.id)
        return true
    }

    private fun getAmount(itemStack: ItemStack): Int {
        return itemStack.tag?.getInt(AMOUNT_NBT) ?: 0
    }

    private fun setAmount(itemStack: ItemStack, amount: Int): Boolean {
        val tag = itemStack.orCreateTag
        tag.putInt(AMOUNT_NBT, amount)
        return true
    }

    fun increaseAmount(itemStack: ItemStack, amount: Int = 1) {
        setAmount(itemStack, getAmount(itemStack) + amount)
    }

    fun isComplete(itemStack: ItemStack): Boolean {
        val gene = getGene(itemStack) ?: return false
        return getAmount(itemStack) >= gene.amountNeeded
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {
        val gene = getGene(pStack)

        if (gene == null) {
            pTooltipComponents.add(
                Component
                    .translatable("tooltip.geneticsresequenced.plasmid.empty")
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
            return
        }

        val amountNeeded = gene.amountNeeded
        val amount = getAmount(pStack)

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
            val stack = ItemStack(this)
            setGene(stack, gene)
            setAmount(stack, gene.amountNeeded)
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