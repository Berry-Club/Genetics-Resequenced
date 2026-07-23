package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
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
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class DupeCellJeiRecipe(
	val cellStack: ItemStack,
	val amountToCreate: Int
) {

	val ingredient: ItemStack = cellStack
	val input: ItemStack = BrewingRecipes.substratePotionStack
	val output: ItemStack = cellStack.copyWithCount(amountToCreate)
	val tooltip: Component = ModRecipeLang.SUBSTRATE.toComponent().withStyle(ChatFormatting.GRAY)

	fun getId(): Identifier {
		var string = "/substrate_dupe/"

		val entityType = EntityDnaItem.getEntityType(cellStack) ?: error("Cell stack has no entity type!")
		string += EntityType.getKey(entityType).toString().replace(':', '/')

		if (cellStack.item == ModItems.GMO_CELL.get()) {
			val geneHolder = DnaHelixItem.getGeneHolder(cellStack)
				?: error("GMO Cell stack has no gene!")
			string += "/" + geneHolder.key!!.identifier().toString().replace(':', '/')
		}

		return GeneticsResequenced.modId(string)
	}

	companion object {
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
