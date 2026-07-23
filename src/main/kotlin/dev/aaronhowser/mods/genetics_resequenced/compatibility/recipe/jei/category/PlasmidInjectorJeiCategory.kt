package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.PlasmidInjectorJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.Identifier

class PlasmidInjectorJeiCategory(
	recipeType: RecipeType<PlasmidInjectorJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<PlasmidInjectorJeiRecipe>(
	recipeType,
	ModRecipeLang.PLASMID_INJECTOR.toComponent(),
	guiHelper.createDrawableItemLike(ModBlocks.PLASMID_INJECTOR),
	102,
	18
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: PlasmidInjectorJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(0, 0).setStandardSlotBackground().addItemStack(recipe.plasmid)
		builder.addInputSlot(24, 0).setStandardSlotBackground().addItemStack(recipe.syringeBefore)
		builder.addOutputSlot(78, 0).setStandardSlotBackground().addItemStack(recipe.syringeAfter)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: PlasmidInjectorJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(48, 1)
	}

	override fun getTooltip(
		tooltip: ITooltipBuilder,
		recipe: PlasmidInjectorJeiRecipe,
		recipeSlotsView: IRecipeSlotsView,
		mouseX: Double,
		mouseY: Double
	) {
		if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) {
			tooltip.add(recipe.tooltip)
		}
	}

	override fun getRegistryName(recipe: PlasmidInjectorJeiRecipe): Identifier = recipe.getId()
}
