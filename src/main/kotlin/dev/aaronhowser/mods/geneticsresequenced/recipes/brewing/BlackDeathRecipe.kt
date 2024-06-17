package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraftforge.common.brewing.IBrewingRecipe

class BlackDeathRecipe : IBrewingRecipe {

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false

        val inputPotion = PotionUtils.getPotion(pBottomSlot)

        return inputPotion != ModPotions.VIRAL_AGENTS
    }

    companion object {
        val allBadGenes = Gene.getRegistry().filter { it.isNegative && it.isActive } - ModGenes.blackDeath
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        val item = pTopSlot.item
        if (item != ModItems.SYRINGE.get() && item != ModItems.METAL_SYRINGE.get()) return false

        if (SyringeItem.isContaminated(pTopSlot)) return false

        val syringeGenes = SyringeItem.getGenes(pTopSlot)
        return syringeGenes.containsAll(allBadGenes)
    }

    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val output = ModItems.DNA_HELIX.itemStack.setGene(ModGenes.blackDeath)

        return output
    }

}