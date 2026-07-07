package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BasicIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class BasicIncubatorJeiRecipe(
	val id: ResourceLocation,
	val ingredient: Ingredient,
	val input: Ingredient,
	val output: ItemStack
) {

	companion object {
		val TYPE: RecipeType<BasicIncubatorJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "incubator", BasicIncubatorJeiRecipe::class.java)

		fun getAllRecipes(recipeManager: RecipeManager): List<BasicIncubatorJeiRecipe> {
			return BasicIncubatorRecipe.getBasicRecipes(recipeManager)
				.map {
					BasicIncubatorJeiRecipe(
						it.id,
						it.value.topIngredient,
						it.value.bottomIngredient,
						it.value.outputStack
					)
				}
		}
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<BasicIncubatorJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemLike(ModBlocks.INCUBATOR)

		override fun getRecipeType(): RecipeType<BasicIncubatorJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.INCUBATOR.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 65
		override fun getHeight(): Int = 61

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: BasicIncubatorJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 28, 2)
				.setStandardSlotBackground()
				.addIngredients(recipe.ingredient)

			builder.addSlot(RecipeIngredientRole.INPUT, 5, 36)
				.setStandardSlotBackground()
				.addIngredients(recipe.input)

			builder.addSlot(RecipeIngredientRole.OUTPUT, 51, 36)
				.setStandardSlotBackground()
				.addItemStack(recipe.output)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: BasicIncubatorJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(23, 31)
		}

		override fun getRegistryName(recipe: BasicIncubatorJeiRecipe): ResourceLocation = recipe.id
	}

}
