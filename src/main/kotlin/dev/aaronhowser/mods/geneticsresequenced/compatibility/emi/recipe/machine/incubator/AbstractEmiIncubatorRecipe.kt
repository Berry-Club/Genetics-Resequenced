package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.incubator

import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items

abstract class AbstractEmiIncubatorRecipe : EmiRecipe {

    companion object {
        val BACKGROUND: ResourceLocation =
            ResourceLocation.withDefaultNamespace("textures/gui/container/brewing_stand.png")
    }

    abstract val ingredient: EmiIngredient
    abstract val input: EmiIngredient
    abstract val output: EmiStack

    open val tertiaryItem: EmiStack = EmiStack.of(Items.BLAZE_POWDER)

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

    open val tooltips: List<Component> = emptyList()

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(BACKGROUND, 0, 0, 103, 61, 16, 14)
        widgets.addSlot(tertiaryItem, 0, 2).drawBack(false)
        widgets.addSlot(input, 39, 36).drawBack(false)
        widgets.addSlot(ingredient, 62, 2).drawBack(false)
        widgets.addSlot(output, 85, 36).drawBack(false).recipeContext(this)

        widgets.addTooltipText(tooltips, 0, 0, displayWidth, displayHeight)
    }

}