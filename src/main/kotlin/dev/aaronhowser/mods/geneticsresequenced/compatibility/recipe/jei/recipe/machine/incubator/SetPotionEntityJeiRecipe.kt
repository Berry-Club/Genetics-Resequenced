package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
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
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class SetPotionEntityJeiRecipe(
	val entityType: EntityType<*>,
	val isMutation: Boolean
) {

	val ingredient: Ingredient
	val input: Ingredient
	val output: ItemStack

	init {
		val cellStack = ModItems.CELL.toStack()
		EntityDnaItem.setEntityType(cellStack, entityType)
		ingredient = Ingredient.of(cellStack)

		val potionStack = if (isMutation) BrewingRecipes.mutationPotionStack else BrewingRecipes.cellGrowthPotionStack
		input = Ingredient.of(potionStack)

		EntityDnaItem.setEntityType(potionStack, entityType)
		output = potionStack
	}

	fun getId(): ResourceLocation {
		val potionString = if (isMutation) "mutation" else "pcg"
		val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')

		return GeneticsResequenced.modResource("/set_potion_entity/$potionString/$entityTypeString")
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<SetPotionEntityJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemStack(BrewingRecipes.cellGrowthPotionStack)

		override fun getRecipeType(): RecipeType<SetPotionEntityJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.SET_ENTITY.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 65
		override fun getHeight(): Int = 61

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: SetPotionEntityJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 28, 2).setStandardSlotBackground().addIngredients(recipe.ingredient)
			builder.addSlot(RecipeIngredientRole.INPUT, 5, 36).setStandardSlotBackground().addIngredients(recipe.input)
			builder.addSlot(RecipeIngredientRole.OUTPUT, 51, 36).setStandardSlotBackground().addItemStack(recipe.output)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: SetPotionEntityJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(23, 31)
		}

		override fun getRegistryName(recipe: SetPotionEntityJeiRecipe): ResourceLocation = recipe.getId()
	}

	companion object {
		val TYPE: RecipeType<SetPotionEntityJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "set_entity", SetPotionEntityJeiRecipe::class.java)

		fun getAllRecipes(): List<SetPotionEntityJeiRecipe> {
			return EntityDnaItem.VALID_ENTITY_TYPES.flatMap {
				listOf(
					SetPotionEntityJeiRecipe(it, isMutation = false),
					SetPotionEntityJeiRecipe(it, isMutation = true)
				)
			}
		}
	}
}
