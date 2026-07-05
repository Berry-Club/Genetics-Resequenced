package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.emi.recipe.machine.incubator

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.emi.ModEmiPlugin
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.item.DnaHelixItem
import dev.aaronhowser.mods.genetics_resequenced.item.EntityDnaItem
import dev.aaronhowser.mods.genetics_resequenced.item.GmoCell
import dev.aaronhowser.mods.genetics_resequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class DupeCellEmiRecipe(
	val cellStack: ItemStack,
	val amountToCreate: Int
) : AbstractEmiIncubatorRecipe() {

	companion object {
		fun getAllRecipes(recipeManager: RecipeManager): List<DupeCellEmiRecipe> {
			val recipes = mutableListOf<DupeCellEmiRecipe>()

			val allEntityTypes = EntityDnaItem.VALID_ENTITY_TYPES

			for (entityType in allEntityTypes) {
				val cellStack = ModItems.CELL.toStack()
				EntityDnaItem.setEntityType(cellStack, entityType)

				recipes.add(DupeCellEmiRecipe(cellStack, 8))
			}

			val allGmoRecipes = GmoRecipe.getGmoRecipes(recipeManager)
			for (recipe in allGmoRecipes) {
				val entityType = recipe.value.entityType
				val goodGene = recipe.value.idealGeneRk

				val gmoCellStack = ModItems.GMO_CELL.toStack()
				GmoCell.setDetails(
					gmoCellStack,
					entityType,
					goodGene.getHolderOrThrow(ClientUtil.localRegistryAccess!!)
				)

				recipes.add(DupeCellEmiRecipe(gmoCellStack, 4))
			}

			return recipes
		}
	}

	override val ingredient: EmiIngredient = EmiIngredient.of(Ingredient.of(cellStack))
	override val input: EmiIngredient = EmiIngredient.of(Ingredient.of(BrewingRecipes.substratePotionStack))
	override val output: EmiStack = EmiStack.of(cellStack.copyWithCount(amountToCreate))

	override fun getCategory(): EmiRecipeCategory {
		return ModEmiPlugin.CELL_DUPE_CATEGORY
	}

	override val tooltips: List<Component> = listOf(
		ModRecipeLang.SUBSTRATE.toComponent().withStyle(ChatFormatting.GRAY)
	)

	override fun getId(): Identifier {
		var string = "/substrate_dupe/"

		val entityType = EntityDnaItem.getEntityType(cellStack) ?: error("Cell stack has no entity type!")
		val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')

		string += entityTypeString

		if (cellStack.item == ModItems.GMO_CELL.get()) {
			val geneHolder = DnaHelixItem.getGeneHolder(cellStack)
				?: error("GMO Cell stack has no gene!")
			val geneString = geneHolder.key!!.identifier().toString().replace(':', '/')

			string += "/$geneString"
		}

		return GeneticsResequenced.modResource(string)
	}

}