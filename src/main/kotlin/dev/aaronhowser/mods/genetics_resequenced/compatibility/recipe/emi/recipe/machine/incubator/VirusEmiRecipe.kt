package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.emi.recipe.machine.incubator

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.emi.ModEmiPlugin
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.item.DnaHelixItem
import dev.aaronhowser.mods.genetics_resequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.VirusRecipe
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class VirusEmiRecipe(
	val inputDnaGeneRk: ResourceKey<Gene>,
	val outputGeneRk: ResourceKey<Gene>
) : AbstractEmiIncubatorRecipe() {

	companion object {
		fun getAllRecipes(recipeManager: RecipeManager): List<VirusEmiRecipe> {
			val allRegularVirusRecipes = VirusRecipe.getVirusRecipes(recipeManager)

			return allRegularVirusRecipes.map {
				VirusEmiRecipe(
					it.value.inputDnaGene,
					it.value.outputGene
				)
			}
		}
	}

	override val ingredient: EmiIngredient
	override val input: EmiIngredient = EmiIngredient.of(Ingredient.of(BrewingRecipes.viralAgentsPotionStack))
	override val output: EmiStack

	override fun getCategory(): EmiRecipeCategory {
		return ModEmiPlugin.VIRUS_CATEGORY
	}

	init {
		val inputStack = DnaHelixItem.getHelixStack(inputDnaGeneRk, ClientUtil.localRegistryAccess!!)
		ingredient = EmiIngredient.of(Ingredient.of(inputStack))

		val outputStack = DnaHelixItem.getHelixStack(outputGeneRk, ClientUtil.localRegistryAccess!!)
		output = EmiStack.of(outputStack)
	}

	override fun getId(): Identifier {
		val inputGeneString = inputDnaGeneRk.identifier().toString().replace(':', '/')
		val outputGeneString = outputGeneRk.identifier().toString().replace(':', '/')

		return GeneticsResequenced.modId("/virus/$inputGeneString/$outputGeneString")
	}

}