package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator.GmoJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.ChatFormatting
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth

class GmoJeiCategory(
	recipeType: IRecipeType<GmoJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<GmoJeiRecipe>(
	recipeType,
	ModRecipeLang.GMO.toComponent(),
	guiHelper.createDrawableItemLike(ModItems.GMO_CELL),
	170,
	42
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: GmoJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(5, 12).setStandardSlotBackground().add(recipe.input)
		builder.addInputSlot(28, 12).setStandardSlotBackground().add(recipe.ingredient)
		builder.addOutputSlot(82, 2)
			.setStandardSlotBackground()
			.add(recipe.success)
			.addRichTooltipCallback { _, tooltip ->
				tooltip.add(ModTooltipLang.GMO_SUCCESS.toComponent().withStyle(ChatFormatting.GREEN))
			}

		builder.addOutputSlot(82, 22)
			.setStandardSlotBackground()
			.add(recipe.failure)
			.addRichTooltipCallback { _, tooltip ->
				tooltip.add(ModTooltipLang.GMO_FAILURE.toComponent().withStyle(ChatFormatting.RED))
			}
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: GmoJeiRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(54, 13)

		val successChance = Mth.ceil(recipe.geneChance * 100)
		builder.addText(
			ModRecipeLang.GMO_SUCCESS_CHANCE
				.toComponent(successChance)
				.withStyle(ChatFormatting.GREEN),
			66, 9
		)
			.setPosition(104, 6)
			.setColor(0xFF000000.toInt())
			.setShadow(true)

		builder.addText(
			ModRecipeLang.GMO_FAILURE_CHANCE
				.toComponent(100 - successChance)
				.withStyle(ChatFormatting.RED),
			66, 9
		)
			.setPosition(104, 26)
			.setColor(0xFF000000.toInt())
			.setShadow(true)
	}

	override fun getTooltip(
		tooltip: ITooltipBuilder,
		recipe: GmoJeiRecipe,
		recipeSlotsView: IRecipeSlotsView,
		mouseX: Double,
		mouseY: Double
	) {
		if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) {
			tooltip.addAll(recipe.tooltips)
		}
	}

	override fun getIdentifier(recipe: GmoJeiRecipe): Identifier = recipe.getId()
}
