package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.category

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.recipe.machine.incubator.SetPotionEntityJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import net.minecraft.resources.ResourceLocation

class SetPotionEntityJeiCategory(
	recipeType: RecipeType<SetPotionEntityJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractJeiIncubatorCategory<SetPotionEntityJeiRecipe>(
	recipeType,
	ModRecipeLang.SET_ENTITY.toComponent(),
	guiHelper.createDrawableItemStack(BrewingRecipes.cellGrowthPotionStack),
	guiHelper
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: SetPotionEntityJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(29, 3).addIngredients(recipe.ingredient)
		builder.addInputSlot(6, 37).addIngredients(recipe.input)
		builder.addOutputSlot(52, 37).addItemStack(recipe.output)
	}

	override fun getRegistryName(recipe: SetPotionEntityJeiRecipe): ResourceLocation = recipe.getId()
}
