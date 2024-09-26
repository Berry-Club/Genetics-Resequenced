package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

abstract class AbstractEmiIncubatorRecipe : EmiRecipe {

    companion object {
        val BACKGROUND: ResourceLocation = OtherUtil.modResource("textures/gui/container/incubator_emi.png")
    }

    abstract val ingredient: EmiIngredient
    abstract val input: EmiIngredient
    abstract val output: EmiStack

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.INCUBATOR_CATEGORY
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(input, ingredient)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(output)
    }

    override fun getDisplayWidth(): Int {
        return 75
    }

    override fun getDisplayHeight(): Int {
        return 61
    }

    open val tooltips: List<Component> = emptyList()

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(BACKGROUND, 5, 0, 65, 61, 55, 14)
        widgets.addSlot(input, 5, 36).drawBack(false)
        widgets.addSlot(ingredient, 28, 2).drawBack(false)
        widgets.addSlot(output, 51, 36).drawBack(false).recipeContext(this)

        widgets.addTooltipText(tooltips, 0, 0, displayWidth, displayHeight)
    }

}