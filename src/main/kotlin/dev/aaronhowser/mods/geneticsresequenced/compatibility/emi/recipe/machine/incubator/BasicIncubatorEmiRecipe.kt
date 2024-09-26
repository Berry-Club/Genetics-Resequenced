package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BasicIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.core.component.DataComponents
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
        val ingredientStack = ingredient.emiStacks.first().itemStack
        val inputStack = input.emiStacks.first().itemStack
        val outputStack = output.itemStack

        val stringBuilder = StringBuilder()
            .append("/incubator/")
            .append(BuiltInRegistries.ITEM.getKey(ingredientStack.item).toString().replace(":", "."))

        if (ingredientStack.has(DataComponents.POTION_CONTENTS)) {
            val potionType = OtherUtil.getPotion(ingredient.emiStacks.first().itemStack)!!.value()
            val potionId = BuiltInRegistries.POTION.getKey(potionType).toString().replace(":", ".")
            stringBuilder
                .append(".")
                .append(potionId)
        }

        stringBuilder
            .append("/")
            .append(BuiltInRegistries.ITEM.getKey(inputStack.item).toString().replace(":", "."))

        if (inputStack.has(DataComponents.POTION_CONTENTS)) {
            val potionType = OtherUtil.getPotion(input.emiStacks.first().itemStack)!!.value()
            val potionId = BuiltInRegistries.POTION.getKey(potionType).toString().replace(":", ".")
            stringBuilder
                .append(".")
                .append(potionId)
        }

        stringBuilder
            .append("/")
            .append(BuiltInRegistries.ITEM.getKey(outputStack.item).toString().replace(":", "."))

        if (outputStack.has(DataComponents.POTION_CONTENTS)) {
            val potionType = OtherUtil.getPotion(output.itemStack)!!.value()
            val potionId = BuiltInRegistries.POTION.getKey(potionType).toString().replace(":", ".")
            stringBuilder
                .append(".")
                .append(potionId)
        }

        return OtherUtil.modResource(stringBuilder.toString())
    }
}