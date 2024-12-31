package dev.aaronhowser.mods.geneticsresequenced.recipe.crafting

import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class UnsetAntiPlasmidRecipe(
    craftingCategory: CraftingBookCategory = CraftingBookCategory.MISC
) : CustomRecipe(craftingCategory) {

    override fun matches(input: CraftingInput, level: Level): Boolean {
        var antiPlasmid: ItemStack? = null

        for (stack in input.items()) {
            if (stack.item == ModItems.ANTI_PLASMID.get() && PlasmidItem.hasGene(stack)) {
                if (antiPlasmid != null) return false
                antiPlasmid = stack
            } else if (!stack.isEmpty) {
                return false
            }
        }

        return antiPlasmid != null
    }

    override fun assemble(input: CraftingInput, provider: HolderLookup.Provider): ItemStack {
        return ModItems.ANTI_PLASMID.toStack()
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        return pWidth * pHeight >= 1
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.UNSET_ANTI_PLASMID.get()
    }

    override fun getType(): RecipeType<*> {
        return ModRecipeTypes.UNSET_ANTI_PLASMID.get()
    }

}