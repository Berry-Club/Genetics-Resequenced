package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeManager

class CellToHelixJeiRecipe(
	val cellStack: ItemStack,
	val helixStack: ItemStack
) {

	fun getId(): ResourceLocation {
		val entityType = EntityDnaItem.getEntityType(cellStack) ?: error("Invalid entity type")
		val entityString = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString().replace(':', '/')

		val stringBuilder = StringBuilder("/cell_to_helix/$entityString")

		if (cellStack.item == ModItems.GMO_CELL.get()) {
			val geneHolder = DnaHelixItem.getGeneHolder(helixStack) ?: error("Invalid gene")
			val geneString = geneHolder.key!!.location().toString().replace(':', '/')
			stringBuilder.append("/gmo/$geneString")
		}

		return GeneticsResequenced.modResource(stringBuilder.toString())
	}

	class Category(guiHelper: IGuiHelper) : IRecipeCategory<CellToHelixJeiRecipe> {
		private val icon: IDrawable = guiHelper.createDrawableItemLike(ModBlocks.DNA_EXTRACTOR)

		override fun getRecipeType(): RecipeType<CellToHelixJeiRecipe> = TYPE
		override fun getTitle(): Component = ModRecipeLang.DNA_EXTRACTOR.toComponent()
		override fun getIcon(): IDrawable = icon
		override fun getWidth(): Int = 76
		override fun getHeight(): Int = 18

		override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: CellToHelixJeiRecipe, focuses: IFocusGroup) {
			builder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.cellStack)

			builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 0)
				.setStandardSlotBackground()
				.addItemStack(recipe.helixStack)
		}

		override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: CellToHelixJeiRecipe, focuses: IFocusGroup) {
			builder.addRecipeArrow().setPosition(26, 1)
		}

		override fun getRegistryName(recipe: CellToHelixJeiRecipe): ResourceLocation = recipe.getId()
	}

	companion object {
		val TYPE: RecipeType<CellToHelixJeiRecipe> =
			RecipeType.create(GeneticsResequenced.MOD_ID, "dna_extractor", CellToHelixJeiRecipe::class.java)

		fun getAllRecipes(recipeManager: RecipeManager): List<CellToHelixJeiRecipe> {
			val recipes = mutableListOf<CellToHelixJeiRecipe>()

			for (entityType in EntityDnaItem.VALID_ENTITY_TYPES) {
				val cellStack = ModItems.CELL.toStack()
				EntityDnaItem.setEntityType(cellStack, entityType)

				val helixStack = ModItems.DNA_HELIX.toStack()
				EntityDnaItem.setEntityType(helixStack, entityType)

				recipes.add(CellToHelixJeiRecipe(cellStack, helixStack))
			}

			for (recipe in GmoRecipe.getGmoRecipes(recipeManager)) {
				val entityType = recipe.value.entityType
				val goodGeneRk = recipe.value.idealGeneRk

				val goodGmoStack = ModItems.GMO_CELL.toStack()
				GmoCell.setDetails(
					goodGmoStack,
					entityType,
					goodGeneRk.getHolderOrThrow(ClientUtil.localRegistryAccess!!)
				)

				recipes.add(
					CellToHelixJeiRecipe(
						goodGmoStack,
						DnaHelixItem.getHelixStack(goodGeneRk, ClientUtil.localRegistryAccess!!)
					)
				)

				val recipeAlreadyExists = recipes.any {
					EntityDnaItem.getEntityType(it.cellStack) == entityType
							&& DnaHelixItem.getGeneHolder(it.helixStack).isGene(ModGenes.BASIC)
				}

				if (recipeAlreadyExists) continue

				val badGmoStack = ModItems.GMO_CELL.toStack()
				GmoCell.setDetails(
					badGmoStack,
					entityType,
					ModGenes.BASIC.getHolderOrThrow(ClientUtil.localRegistryAccess!!)
				)

				recipes.add(
					CellToHelixJeiRecipe(
						badGmoStack,
						DnaHelixItem.getHelixStack(ModGenes.BASIC, ClientUtil.localRegistryAccess!!)
					)
				)
			}

			return recipes.distinctBy(CellToHelixJeiRecipe::getId)
		}
	}
}
