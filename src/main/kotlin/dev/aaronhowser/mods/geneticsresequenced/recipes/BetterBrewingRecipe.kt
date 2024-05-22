package dev.aaronhowser.mods.geneticsresequenced.recipes

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraftforge.common.brewing.IBrewingRecipe

class BetterBrewingRecipe(
    val inputPotion: Potion,
    val ingredient: ItemStack,
    val outputPotion: Potion
) : IBrewingRecipe {

    override fun isInput(input: ItemStack): Boolean = PotionUtils.getPotion(input) == this.inputPotion

    override fun isIngredient(ingredient: ItemStack): Boolean = ingredient == this.ingredient

    override fun getOutput(input: ItemStack, ingredient: ItemStack): ItemStack {
        if (!isInput(input) || !isIngredient(ingredient)) return ItemStack.EMPTY

        val output = input.copy()
        output.tag = CompoundTag()
        PotionUtils.setPotion(output, this.outputPotion)

        return output
    }
}