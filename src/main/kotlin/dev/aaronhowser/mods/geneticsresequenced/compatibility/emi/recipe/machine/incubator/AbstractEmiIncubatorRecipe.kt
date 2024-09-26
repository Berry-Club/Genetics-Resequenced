package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.incubator

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.network.chat.Component

abstract class AbstractEmiIncubatorRecipe : EmiRecipe {

    abstract val ingredient: EmiIngredient
    abstract val input: EmiIngredient
    abstract val output: EmiStack

    override fun getCategory(): EmiRecipeCategory {
        return VanillaEmiRecipeCategories.BREWING
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(input, ingredient)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(output)
    }

    override fun getDisplayWidth(): Int {
        return 102
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    open val tooltips: List<Component> = emptyList()

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addSlot(input, 0, 0)
        widgets.addSlot(ingredient, 24, 0)
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 48, 1)
        widgets.addSlot(output, 78, 0).recipeContext(this)

        widgets.addTooltipText(tooltips, 0, 0, displayWidth, displayHeight)
    }

}