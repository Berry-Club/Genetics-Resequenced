package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items

abstract class AbstractEmiBrewingRecipe : EmiRecipe {

    companion object {
        val BACKGROUND: ResourceLocation =
            ResourceLocation.withDefaultNamespace("textures/gui/container/brewing_stand.png")
    }

    abstract val ingredient: EmiIngredient
    abstract val input: EmiIngredient
    abstract val output: EmiStack

    private val blazePowder: EmiStack = EmiStack.of(Items.BLAZE_POWDER)

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
        return 120
    }

    override fun getDisplayHeight(): Int {
        return 61
    }

    override fun addWidgets(widgets: WidgetHolder) {

        widgets.addTexture(BACKGROUND, 0, 0, 103, 61, 16, 14)
        widgets.addAnimatedTexture(BACKGROUND, 81, 2, 9, 28, 176, 0, 1000 * 20, false, false, false)
        widgets.addAnimatedTexture(BACKGROUND, 47, 0, 12, 29, 185, 0, 700, false, true, false)
        widgets.addTexture(BACKGROUND, 44, 30, 18, 4, 176, 29);
        widgets.addSlot(blazePowder, 0, 2).drawBack(false)
        widgets.addSlot(input, 39, 36).drawBack(false)
        widgets.addSlot(ingredient, 62, 2).drawBack(false)
        widgets.addSlot(output, 85, 36).drawBack(false).recipeContext(this)

    }

}