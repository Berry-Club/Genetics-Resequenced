package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.OrganicMatterToCellJeiRecipe
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

class OrganicMatterToCellJeiCategory(
	recipeType: IRecipeType<OrganicMatterToCellJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<OrganicMatterToCellJeiRecipe>(
	recipeType,
	ModRecipeLang.CELL_ANALYZER.toComponent(),
	guiHelper.createDrawableItemLike(ModBlocks.CELL_ANALYZER),
	76,
	18
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: OrganicMatterToCellJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(0, 0).setStandardSlotBackground().add(recipe.organicMatter)
		builder.addOutputSlot(58, 0).setStandardSlotBackground().add(recipe.cell)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: OrganicMatterToCellJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(26, 1)
	}

	override fun getIdentifier(recipe: OrganicMatterToCellJeiRecipe): Identifier = recipe.getId()
}
