package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator.VirusJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.recipe.BrewingRecipes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import net.minecraft.resources.Identifier

class VirusJeiCategory(
	recipeType: IRecipeType<VirusJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractJeiIncubatorCategory<VirusJeiRecipe>(
	recipeType,
	ModRecipeLang.VIRUS.toComponent(),
	guiHelper.createDrawableItemStack(BrewingRecipes.viralAgentsPotionStack),
	guiHelper
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: VirusJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(29, 3).add(recipe.ingredient)
		builder.addInputSlot(6, 37).add(recipe.input)
		builder.addOutputSlot(52, 37).add(recipe.output)
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

	override fun getIdentifier(recipe: VirusJeiRecipe): Identifier = recipe.id
}
