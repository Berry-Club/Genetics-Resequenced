package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.category

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.recipe.machine.incubator.GmoJeiRecipe
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
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class GmoJeiCategory(
	recipeType: RecipeType<GmoJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<GmoJeiRecipe>(
	recipeType,
	ModRecipeLang.GMO.toComponent(),
	guiHelper.createDrawableItemLike(ModItems.GMO_CELL),
	145,
	42
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: GmoJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(5, 12).setStandardSlotBackground().addItemStack(recipe.input)
		builder.addInputSlot(28, 12).setStandardSlotBackground().addIngredients(recipe.ingredient)
		builder.addOutputSlot(76, 2).setStandardSlotBackground().addItemStack(recipe.success)
		builder.addOutputSlot(76, 22).setStandardSlotBackground().addItemStack(recipe.failure)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: GmoJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(52, 13)

		val successChance = (recipe.geneChance * 100).toInt()
		builder.addText(Component.literal("Success: $successChance%").withStyle(ChatFormatting.GREEN), 60, 9)
			.setPosition(99, 6)
			.setColor(0x000000)
			.setShadow(true)

		builder.addText(Component.literal("Failure: ${100 - successChance}%").withStyle(ChatFormatting.RED), 60, 9)
			.setPosition(99, 26)
			.setColor(0x000000)
			.setShadow(true)
	}

	override fun getTooltip(
		tooltip: ITooltipBuilder,
		recipe: GmoJeiRecipe,
		recipeSlotsView: IRecipeSlotsView,
		mouseX: Double,
		mouseY: Double
	) {
		if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) tooltip.addAll(recipe.tooltips)
	}

	override fun getRegistryName(recipe: GmoJeiRecipe): ResourceLocation = recipe.getId()
}
