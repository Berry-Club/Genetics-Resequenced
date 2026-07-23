package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator.SetPotionEntityJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.recipe.BrewingRecipes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import net.minecraft.resources.Identifier

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
		builder.addInputSlot(29, 3).addItemStack(recipe.ingredient)
		builder.addInputSlot(6, 37).addItemStack(recipe.input)
		builder.addOutputSlot(52, 37).addItemStack(recipe.output)
	}

	override fun getRegistryName(recipe: SetPotionEntityJeiRecipe): Identifier = recipe.getId()
}
