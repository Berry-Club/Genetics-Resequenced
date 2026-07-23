package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.category

import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator.SetPotionEntityJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.ResourceLocation

class SetPotionEntityJeiCategory(
	recipeType: RecipeType<SetPotionEntityJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<SetPotionEntityJeiRecipe>(
	recipeType,
	ModRecipeLang.SET_ENTITY.toComponent(),
	guiHelper.createDrawableItemStack(BrewingRecipes.cellGrowthPotionStack),
	65,
	61
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: SetPotionEntityJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(28, 2).setStandardSlotBackground().addIngredients(recipe.ingredient)
		builder.addInputSlot(5, 36).setStandardSlotBackground().addIngredients(recipe.input)
		builder.addOutputSlot(51, 36).setStandardSlotBackground().addItemStack(recipe.output)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: SetPotionEntityJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(23, 31)
	}

	override fun getRegistryName(recipe: SetPotionEntityJeiRecipe): ResourceLocation = recipe.getId()
}
