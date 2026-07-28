package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.category

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.recipe.machine.PlasmidInfuserJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.ResourceLocation

class PlasmidInfuserJeiCategory(
	recipeType: RecipeType<PlasmidInfuserJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<PlasmidInfuserJeiRecipe>(
	recipeType,
	ModRecipeLang.PLASMID_INFUSER.toComponent(),
	guiHelper.createDrawableItemLike(ModBlocks.PLASMID_INFUSER),
	76,
	18
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: PlasmidInfuserJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(0, 0).setStandardSlotBackground().addItemStack(recipe.helix)
		builder.addOutputSlot(58, 0).setStandardSlotBackground().addItemStack(recipe.plasmid)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: PlasmidInfuserJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(26, 1)
	}

	override fun getTooltip(
		tooltip: ITooltipBuilder,
		recipe: PlasmidInfuserJeiRecipe,
		recipeSlotsView: IRecipeSlotsView,
		mouseX: Double,
		mouseY: Double
	) {
		if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) {
			tooltip.addAll(recipe.tooltips)
		}
	}

	override fun getRegistryName(recipe: PlasmidInfuserJeiRecipe): ResourceLocation = recipe.getId()
}
