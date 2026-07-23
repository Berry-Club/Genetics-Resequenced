package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.category

import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator.DupeCellJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.ResourceLocation

class DupeCellJeiCategory(
	recipeType: RecipeType<DupeCellJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<DupeCellJeiRecipe>(
	recipeType,
	ModRecipeLang.SUBSTRATE_DUPE.toComponent(),
	guiHelper.createDrawableItemLike(ModItems.CELL),
	65,
	61
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: DupeCellJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(28, 2).setStandardSlotBackground().addIngredients(recipe.ingredient)
		builder.addInputSlot(5, 36).setStandardSlotBackground().addIngredients(recipe.input)
		builder.addOutputSlot(51, 36).setStandardSlotBackground().addItemStack(recipe.output)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: DupeCellJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(23, 31)
	}

	override fun getTooltip(
		tooltip: ITooltipBuilder,
		recipe: DupeCellJeiRecipe,
		recipeSlotsView: IRecipeSlotsView,
		mouseX: Double,
		mouseY: Double
	) {
		if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) tooltip.add(recipe.tooltip)
	}

	override fun getRegistryName(recipe: DupeCellJeiRecipe): ResourceLocation = recipe.getId()
}
