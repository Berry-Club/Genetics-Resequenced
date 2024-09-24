package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import net.minecraft.world.item.crafting.Recipe

abstract class IncubatorRecipe : Recipe<IncubatorRecipeInput> {
    override fun canCraftInDimensions(p0: Int, p1: Int): Boolean = true
}