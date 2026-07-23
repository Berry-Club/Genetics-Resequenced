package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.category

import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator.VirusJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.ResourceLocation

class VirusJeiCategory(
	recipeType: RecipeType<VirusJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<VirusJeiRecipe>(
	recipeType,
	ModRecipeLang.VIRUS.toComponent(),
	guiHelper.createDrawableItemStack(BrewingRecipes.viralAgentsPotionStack),
	72,
	61
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: VirusJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(28, 2).setStandardSlotBackground().addIngredients(recipe.ingredient)
		builder.addInputSlot(5, 36).setStandardSlotBackground().addIngredients(recipe.input)
		builder.addOutputSlot(51, 36).setOutputSlotBackground().addItemStack(recipe.output)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: VirusJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(23, 34)
	}

	override fun getTooltip(
		tooltip: ITooltipBuilder,
		recipe: VirusJeiRecipe,
		recipeSlotsView: IRecipeSlotsView,
		mouseX: Double,
		mouseY: Double
	) {
		if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) tooltip.addAll(recipe.tooltips)
	}

	override fun getRegistryName(recipe: VirusJeiRecipe): ResourceLocation = recipe.id
}
