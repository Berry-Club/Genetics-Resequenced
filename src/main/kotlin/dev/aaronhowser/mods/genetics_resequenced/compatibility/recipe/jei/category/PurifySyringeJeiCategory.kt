package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.PurifySyringeJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.Identifier

class PurifySyringeJeiCategory(
	recipeType: RecipeType<PurifySyringeJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<PurifySyringeJeiRecipe>(
	recipeType,
	ModRecipeLang.BLOOD_PURIFIER.toComponent(),
	guiHelper.createDrawableItemLike(ModBlocks.BLOOD_PURIFIER),
	76,
	18
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: PurifySyringeJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(0, 0)
			.setStandardSlotBackground()
			.addItemStack(recipe.contaminatedSyringe)

		builder.addOutputSlot(58, 0)
			.setStandardSlotBackground()
			.addItemStack(recipe.decontaminatedSyringe)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: PurifySyringeJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(26, 1)
	}

	override fun getRegistryName(recipe: PurifySyringeJeiRecipe): Identifier = recipe.getId()
}
