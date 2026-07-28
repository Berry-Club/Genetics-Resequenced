package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.category

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.recipe.machine.PurifySyringeJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.ResourceLocation

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

	override fun getRegistryName(recipe: PurifySyringeJeiRecipe): ResourceLocation = recipe.getId()
}
