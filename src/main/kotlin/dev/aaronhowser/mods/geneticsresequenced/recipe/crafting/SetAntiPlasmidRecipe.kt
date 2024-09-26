package dev.aaronhowser.mods.geneticsresequenced.recipe.crafting

import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class SetAntiPlasmidRecipe(
    craftingCategory: CraftingBookCategory = CraftingBookCategory.MISC
) : CustomRecipe(craftingCategory) {

    override fun matches(input: CraftingInput, level: Level): Boolean {

        var plasmid: ItemStack? = null
        var antiPlasmid: ItemStack? = null

        for (stack in input.items()) {
            if (stack.item == ModItems.PLASMID.get()) {
                if (plasmid != null) return false
                plasmid = stack
            }
            if (stack.item == ModItems.ANTI_PLASMID.get()) {
                if (antiPlasmid != null) return false
                antiPlasmid = stack
            }
            if (plasmid != null && antiPlasmid != null) break
        }

        if (plasmid == null || antiPlasmid == null) return false

        return !PlasmidItem.hasGene(antiPlasmid) && PlasmidItem.isComplete(plasmid)
    }

    override fun assemble(input: CraftingInput, provider: HolderLookup.Provider): ItemStack {

        var plasmidStack: ItemStack? = null
        for (stack in input.items()) {
            if (stack.item == ModItems.PLASMID.get()) {
                if (plasmidStack != null) return ItemStack.EMPTY
                plasmidStack = stack
            }
        }
        if (plasmidStack == null) return ItemStack.EMPTY

        val plasmidGene = PlasmidItem.getGene(plasmidStack) ?: return ItemStack.EMPTY

        val antiPlasmidStack = ModItems.ANTI_PLASMID.toStack()
        PlasmidItem.setGene(antiPlasmidStack, plasmidGene)

        return antiPlasmidStack
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        return pWidth * pHeight >= 2
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.SET_ANTI_PLASMID.get()
    }

    override fun getType(): RecipeType<*> {
        return ModRecipeTypes.SET_ANTI_PLASMID.get()
    }

}