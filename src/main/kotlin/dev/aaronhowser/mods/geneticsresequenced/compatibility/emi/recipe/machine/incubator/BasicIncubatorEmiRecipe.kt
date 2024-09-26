package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BasicIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeManager

class BasicIncubatorEmiRecipe(
    recipe: BasicIncubatorRecipe
) : AbstractEmiIncubatorRecipe() {

    override val ingredient: EmiIngredient = EmiIngredient.of(recipe.topSlotIngredient)
    override val input: EmiIngredient = EmiIngredient.of(recipe.bottomSlotIngredient)
    override val output: EmiStack = EmiStack.of(recipe.outputStack)

    companion object {
        fun getAllRecipes(recipeManager: RecipeManager): List<BasicIncubatorEmiRecipe> {
            val allRegularVirusRecipes = BasicIncubatorRecipe.getBasicRecipes(recipeManager)

            return allRegularVirusRecipes.map {
                BasicIncubatorEmiRecipe(it.value)
            }
        }
    }

    override fun getId(): ResourceLocation {
        val ingredientItem = ingredient.emiStacks.first().itemStack.item
        val inputItem = input.emiStacks.first().itemStack.item
        val outputItem = output.itemStack.item

        val stringBuilder = StringBuilder()
            .append("/incubator/")
            .append(BuiltInRegistries.ITEM.getKey(ingredientItem).toString().replace(":", "."))
            .append("/")
            .append(BuiltInRegistries.ITEM.getKey(inputItem).toString().replace(":", "."))
            .append("/")
            .append(BuiltInRegistries.ITEM.getKey(outputItem).toString().replace(":", "."))

        return OtherUtil.modResource(stringBuilder.toString())
    }
}