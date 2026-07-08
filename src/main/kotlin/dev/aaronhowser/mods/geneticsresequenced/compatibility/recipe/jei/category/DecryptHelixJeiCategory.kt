package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.DecryptHelixJeiRecipe
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class DecryptHelixJeiCategory(
	recipeType: RecipeType<DecryptHelixJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<DecryptHelixJeiRecipe>(
	recipeType,
	ModRecipeLang.DNA_DECRYPTOR.toComponent(),
	guiHelper.createDrawableItemLike(ModBlocks.DNA_DECRYPTOR),
	116,
	18
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: DecryptHelixJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(40, 0).setStandardSlotBackground().addItemStack(recipe.encryptedHelix)
		builder.addOutputSlot(98, 0).setStandardSlotBackground().addItemStack(recipe.decryptedHelix)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: DecryptHelixJeiRecipe, focuses: IFocusGroup) {
		builder.addText(Component.literal(String.format("%.2f%%", recipe.chance * 100)), 38, 9)
			.setPosition(0, 4)
			.setColor(0x3E3E3E)

		builder.addRecipeArrow().setPosition(66, 1)
	}

	override fun getRegistryName(recipe: DecryptHelixJeiRecipe): ResourceLocation = recipe.getId()
}
