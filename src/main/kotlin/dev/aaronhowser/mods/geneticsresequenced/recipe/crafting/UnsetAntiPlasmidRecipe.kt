package dev.aaronhowser.mods.geneticsresequenced.recipe.crafting

import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class UnsetAntiPlasmidRecipe(
    craftingCategory: CraftingBookCategory = CraftingBookCategory.MISC
) : CustomRecipe(craftingCategory) {

    override fun matches(input: CraftingInput, level: Level): Boolean {
        var antiPlasmid: ItemStack? = null

        for (stack in input.items()) {
            if (stack.item == ModItems.ANTI_PLASMID.get()) {
                if (antiPlasmid != null) return false
                antiPlasmid = stack
            } else if (!stack.isEmpty) {
                return false
            }
        }

        if (antiPlasmid == null) return false

        return PlasmidItem.hasGene(antiPlasmid)
    }

    override fun assemble(input: CraftingInput, provider: HolderLookup.Provider): ItemStack {
        return ModItems.ANTI_PLASMID.toStack()
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        return pWidth * pHeight >= 2
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.UNSET_ANTI_PLASMID.get()
    }

}