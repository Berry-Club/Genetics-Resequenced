package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.BasicIncubatorRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.RecipeHolder

class BasicIncubatorJeiCategory(
	recipeType: RecipeType<RecipeHolder<BasicIncubatorRecipe>>,
	guiHelper: IGuiHelper
) : AbstractJeiIncubatorCategory<RecipeHolder<BasicIncubatorRecipe>>(
	recipeType,
	ModRecipeLang.INCUBATOR.toComponent(),
	guiHelper.createDrawableItemLike(ModBlocks.INCUBATOR),
	guiHelper
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<BasicIncubatorRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		builder.addInputSlot(29, 3).addIngredients(recipe.topIngredient)
		builder.addInputSlot(6, 37).addIngredients(recipe.bottomIngredient)
		builder.addOutputSlot(52, 37).addItemStack(recipe.outputStack)
	}

	override fun getRegistryName(recipe: RecipeHolder<BasicIncubatorRecipe>): Identifier = recipe.id.identifier()
}
