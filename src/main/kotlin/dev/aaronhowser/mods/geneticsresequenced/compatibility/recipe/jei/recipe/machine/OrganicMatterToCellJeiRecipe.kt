package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack

class OrganicMatterToCellJeiRecipe(
	val entityType: EntityType<*>
) {

	val organicMatter: ItemStack
	val cell: ItemStack

	init {
		organicMatter = ModItems.ORGANIC_MATTER.toStack()
		EntityDnaItem.setEntityType(organicMatter, entityType)

		cell = ModItems.CELL.toStack()
		EntityDnaItem.setEntityType(cell, entityType)
	}

	fun getId(): ResourceLocation {
		val entityString = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString().replace(':', '/')
		return GeneticsResequenced.modResource("/cell_analyzer/$entityString")
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<OrganicMatterToCellJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemLike(ModBlocks.CELL_ANALYZER)

		override fun getRecipeType(): RecipeType<OrganicMatterToCellJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.CELL_ANALYZER.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 76
		override fun getHeight(): Int = 18

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: OrganicMatterToCellJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.organicMatter)

			builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.cell)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: OrganicMatterToCellJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(26, 1)
		}

		override fun getRegistryName(recipe: OrganicMatterToCellJeiRecipe): ResourceLocation = recipe.getId()
	}

	companion object {
		val TYPE: RecipeType<OrganicMatterToCellJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "cell_analyzer", OrganicMatterToCellJeiRecipe::class.java)

		fun getAllRecipes(): List<OrganicMatterToCellJeiRecipe> {
			return EntityDnaItem.VALID_ENTITY_TYPES
				.map(::OrganicMatterToCellJeiRecipe)
				.distinctBy(OrganicMatterToCellJeiRecipe::getId)
		}
	}
}
