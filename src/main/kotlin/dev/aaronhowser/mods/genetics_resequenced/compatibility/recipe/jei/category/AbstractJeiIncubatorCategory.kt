package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.category

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier

abstract class AbstractJeiIncubatorCategory<T : Any>(
	recipeType: RecipeType<T>,
	title: Component,
	icon: IDrawable,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<T>(recipeType, title, icon, 75, 61) {

	private val background = guiHelper.createDrawable(
		BACKGROUND,
		55,
		14,
		65,
		61
	)

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: T, focuses: IFocusGroup) {
		builder.addDrawable(background, 5, 0)
	}

	companion object {
		val BACKGROUND: Identifier = GeneticsResequenced.modId("textures/gui/container/incubator_background.png")
	}

}
