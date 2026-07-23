package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.CellToHelixJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.Identifier

class CellToHelixJeiCategory(
	recipeType: IRecipeType<CellToHelixJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<CellToHelixJeiRecipe>(
	recipeType,
	ModRecipeLang.DNA_EXTRACTOR.toComponent(),
	guiHelper.createDrawableItemLike(ModBlocks.DNA_EXTRACTOR),
	76,
	18
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: CellToHelixJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(0, 0).setStandardSlotBackground().add(recipe.cellStack)
		builder.addOutputSlot(58, 0).setStandardSlotBackground().add(recipe.helixStack)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: CellToHelixJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(26, 1)
	}

	override fun getIdentifier(recipe: CellToHelixJeiRecipe): Identifier = recipe.getId()
}
