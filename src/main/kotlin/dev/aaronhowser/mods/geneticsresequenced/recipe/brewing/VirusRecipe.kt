package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.brewing.IBrewingRecipe

class VirusRecipe(
    val inputDnaGene: Holder<Gene>,
    val outputGene: Holder<Gene>
) : IBrewingRecipe {

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false

        val inputPotion = OtherUtil.getPotion(pBottomSlot) ?: return false

        return inputPotion == ModPotions.VIRAL_AGENTS
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        if (pTopSlot.item != ModItems.DNA_HELIX.get()) return false

        return DnaHelixItem.getGene(pTopSlot) == inputDnaGene
    }

    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val output = DnaHelixItem.setGene(ModItems.DNA_HELIX.toStack(), outputGene)

        return output
    }

}