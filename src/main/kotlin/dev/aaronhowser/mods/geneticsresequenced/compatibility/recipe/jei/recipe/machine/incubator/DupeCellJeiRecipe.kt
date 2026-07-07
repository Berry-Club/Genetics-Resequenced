package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.builder.ITooltipBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class DupeCellJeiRecipe(
	val cellStack: ItemStack,
	val amountToCreate: Int
) {

	val ingredient: Ingredient = Ingredient.of(cellStack)
	val input: Ingredient = Ingredient.of(BrewingRecipes.substratePotionStack)
	val output: ItemStack = cellStack.copyWithCount(amountToCreate)
	val tooltip: Component = ModRecipeLang.SUBSTRATE.toComponent().withStyle(ChatFormatting.GRAY)

	fun getId(): ResourceLocation {
		var string = "/substrate_dupe/"

		val entityType = EntityDnaItem.getEntityType(cellStack) ?: error("Cell stack has no entity type!")
		string += EntityType.getKey(entityType).toString().replace(':', '/')

		if (cellStack.item == ModItems.GMO_CELL.get()) {
			val geneHolder = DnaHelixItem.getGeneHolder(cellStack)
				?: error("GMO Cell stack has no gene!")
			string += "/" + geneHolder.key!!.location().toString().replace(':', '/')
		}

		return GeneticsResequenced.modResource(string)
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<DupeCellJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemLike(ModItems.CELL)

		override fun getRecipeType(): RecipeType<DupeCellJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.SUBSTRATE_DUPE.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 65
		override fun getHeight(): Int = 61

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: DupeCellJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 28, 2).setStandardSlotBackground().addIngredients(recipe.ingredient)
			builder.addSlot(RecipeIngredientRole.INPUT, 5, 36).setStandardSlotBackground().addIngredients(recipe.input)
			builder.addSlot(RecipeIngredientRole.OUTPUT, 51, 36).setStandardSlotBackground().addItemStack(recipe.output)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: DupeCellJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(23, 31)
		}

		override fun getTooltip(
			tooltip: ITooltipBuilder,
			recipe: DupeCellJeiRecipe,
			recipeSlotsView: IRecipeSlotsView,
			mouseX: Double,
			mouseY: Double
		) {
			if (mouseX >= 0 && mouseY >= 0 && mouseX < width && mouseY < height) tooltip.add(recipe.tooltip)
		}

		override fun getRegistryName(recipe: DupeCellJeiRecipe): ResourceLocation = recipe.getId()
	}

	companion object {
		val TYPE: RecipeType<DupeCellJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "cell_dupe", DupeCellJeiRecipe::class.java)

		fun getAllRecipes(recipeManager: RecipeManager): List<DupeCellJeiRecipe> {
			val recipes = mutableListOf<DupeCellJeiRecipe>()

			for (entityType in EntityDnaItem.VALID_ENTITY_TYPES) {
				val cellStack = ModItems.CELL.toStack()
				EntityDnaItem.setEntityType(cellStack, entityType)

				recipes.add(DupeCellJeiRecipe(cellStack, 8))
			}

			for (recipe in GmoRecipe.getGmoRecipes(recipeManager)) {
				val entityType = recipe.value.entityType
				val goodGene = recipe.value.idealGeneRk

				val gmoCellStack = ModItems.GMO_CELL.toStack()
				GmoCell.setDetails(
					gmoCellStack,
					entityType,
					goodGene.getHolderOrThrow(ClientUtil.localRegistryAccess!!)
				)

				recipes.add(DupeCellJeiRecipe(gmoCellStack, 4))
			}

			return recipes.distinctBy(DupeCellJeiRecipe::getId)
		}
	}
}
