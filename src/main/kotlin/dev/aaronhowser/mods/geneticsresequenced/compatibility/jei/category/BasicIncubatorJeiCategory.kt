package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.category

import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BasicIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

class BasicIncubatorJeiCategory(
	recipeType: RecipeType<RecipeHolder<BasicIncubatorRecipe>>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<BasicIncubatorRecipe>>(
	recipeType,
	ModRecipeLang.INCUBATOR.toComponent(),
	guiHelper.createDrawableItemLike(ModBlocks.INCUBATOR),
	65,
	61
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<BasicIncubatorRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		builder.addInputSlot(28, 2).setStandardSlotBackground().addIngredients(recipe.topIngredient)
		builder.addInputSlot(5, 36).setStandardSlotBackground().addIngredients(recipe.bottomIngredient)
		builder.addOutputSlot(51, 36).setStandardSlotBackground().addItemStack(recipe.outputStack)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<BasicIncubatorRecipe>, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(23, 31)
	}

	override fun getRegistryName(recipe: RecipeHolder<BasicIncubatorRecipe>): ResourceLocation = recipe.id
}
