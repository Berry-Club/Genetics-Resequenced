package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.recipe.machine.incubator

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.item.DnaHelixItem
import dev.aaronhowser.mods.genetics_resequenced.item.SyringeItem
import dev.aaronhowser.mods.genetics_resequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.BlackDeathRecipe
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.VirusRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.cow.Cow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class VirusJeiRecipe(
	val id: Identifier,
	val ingredient: ItemStack,
	val input: ItemStack,
	val output: ItemStack,
	val tooltips: List<Component> = emptyList()
) {

	companion object {
		fun getAllRecipes(recipeManager: RecipeManager): List<VirusJeiRecipe> {
			val recipes = VirusRecipe.getVirusRecipes(recipeManager)
				.map {
					createVirusRecipe(it.value.inputDnaGene, it.value.outputGene)
				}
				.toMutableList()

			recipes.add(blackDeathRecipe(isMetal = false))
			recipes.add(blackDeathRecipe(isMetal = true))

			return recipes
		}

		private fun createVirusRecipe(
			inputDnaGeneRk: ResourceKey<Gene>,
			outputGeneRk: ResourceKey<Gene>
		): VirusJeiRecipe {
			val inputGeneString = inputDnaGeneRk.identifier().toString().replace(':', '/')
			val outputGeneString = outputGeneRk.identifier().toString().replace(':', '/')

			return VirusJeiRecipe(
				id = GeneticsResequenced.modId("/virus/$inputGeneString/$outputGeneString"),
				ingredient = DnaHelixItem.getHelixStack(inputDnaGeneRk, ClientUtil.localRegistryAccess!!),
				input = BrewingRecipes.viralAgentsPotionStack,
				output = DnaHelixItem.getHelixStack(outputGeneRk, ClientUtil.localRegistryAccess!!)
			)
		}

		private fun blackDeathRecipe(isMetal: Boolean): VirusJeiRecipe {
			val syringeStack = if (isMetal) ModItems.METAL_SYRINGE.toStack() else ModItems.SYRINGE.toStack()

			val localPlayer = AaronClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")
			val entity = if (isMetal) Cow(EntityType.COW, localPlayer.level()) else localPlayer

			SyringeItem.setEntity(syringeStack, entity, setContaminated = false)

			for (gene in BlackDeathRecipe().getRequiredGenes(ClientUtil.localRegistryAccess!!)) {
				SyringeItem.addGene(syringeStack, gene)
			}

			val type = if (isMetal) "/metal" else ""

			return VirusJeiRecipe(
				id = GeneticsResequenced.modId("/brewing/black_death$type"),
				ingredient = syringeStack,
				input = BrewingRecipes.viralAgentsPotionStack,
				output = DnaHelixItem.getHelixStack(ModGenes.BLACK_DEATH, ClientUtil.localRegistryAccess!!),
				tooltips = listOf(ModRecipeLang.BLACK_DEATH.toComponent().withStyle(ChatFormatting.GRAY))
			)
		}
	}
}
