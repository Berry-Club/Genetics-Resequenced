package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.category

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.ModJeiPlugin
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.recipe.machine.incubator.DupeCellJeiRecipe
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
	private val guiHelper: IGuiHelper
) : AbstractRecipeCategory<DupeCellJeiRecipe>(
	recipeType,
	ModRecipeLang.SUBSTRATE_DUPE.toComponent(),
	guiHelper.createDrawableItemLike(ModItems.CELL),
	75,
	61
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: DupeCellJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(29, 3).addIngredients(recipe.ingredient)
		builder.addInputSlot(6, 37).addIngredients(recipe.input)
		builder.addOutputSlot(52, 37).addItemStack(recipe.output)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: DupeCellJeiRecipe, focuses: IFocusGroup) {
		val background = guiHelper.createDrawable(
			ModJeiPlugin.INCUBATOR_BACKGROUND,
			55,
			14,
			65,
			61
		)

		builder.addDrawable(background, 5, 0)
	}

	override fun getTooltip(
		tooltip: ITooltipBuilder,
		recipe: DupeCellJeiRecipe,
		recipeSlotsView: IRecipeSlotsView,
		mouseX: Double,
		mouseY: Double
	) {
		if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) {
			tooltip.add(recipe.tooltip)
		}
	}

	override fun getRegistryName(recipe: DupeCellJeiRecipe): ResourceLocation = recipe.getId()

}
