package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.DecryptHelixJeiRecipe
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

class DecryptHelixJeiCategory(
	recipeType: IRecipeType<DecryptHelixJeiRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<DecryptHelixJeiRecipe>(
	recipeType,
	ModRecipeLang.DNA_DECRYPTOR.toComponent(),
	guiHelper.createDrawableItemLike(ModBlocks.DNA_DECRYPTOR),
	104,
	18
) {
	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: DecryptHelixJeiRecipe, focuses: IFocusGroup) {
		builder.addInputSlot(34, 0).setStandardSlotBackground().add(recipe.encryptedHelix)
		builder.addOutputSlot(86, 0).setStandardSlotBackground().add(recipe.decryptedHelix)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: DecryptHelixJeiRecipe, focuses: IFocusGroup) {
		builder.addText(Component.literal(String.format("%.2f%%", recipe.chance * 100)), 32, 9)
			.setPosition(0, 4)
			.setColor(0xFF3E3E3E.toInt())

		builder.addRecipeArrow().setPosition(57, 1)
	}

	override fun getIdentifier(recipe: DecryptHelixJeiRecipe): Identifier = recipe.getId()
}
