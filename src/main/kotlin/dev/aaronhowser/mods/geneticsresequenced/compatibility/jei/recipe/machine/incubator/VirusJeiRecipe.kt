package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.recipe.machine.incubator

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BlackDeathRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.VirusRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class VirusJeiRecipe(
	val id: ResourceLocation,
	val ingredient: Ingredient,
	val input: Ingredient,
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
			val inputGeneString = inputDnaGeneRk.location().toString().replace(':', '/')
			val outputGeneString = outputGeneRk.location().toString().replace(':', '/')

			return VirusJeiRecipe(
				id = GeneticsResequenced.modResource("/virus/$inputGeneString/$outputGeneString"),
				ingredient = Ingredient.of(DnaHelixItem.getHelixStack(inputDnaGeneRk, ClientUtil.localRegistryAccess!!)),
				input = Ingredient.of(BrewingRecipes.viralAgentsPotionStack),
				output = DnaHelixItem.getHelixStack(outputGeneRk, ClientUtil.localRegistryAccess!!)
			)
		}

		private fun blackDeathRecipe(isMetal: Boolean): VirusJeiRecipe {
			val syringeStack = if (isMetal) ModItems.METAL_SYRINGE.toStack() else ModItems.SYRINGE.toStack()

			val localPlayer = AaronClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")
			val entity = if (isMetal) Cow(EntityType.COW, localPlayer.level()) else localPlayer

			SyringeItem.setEntity(syringeStack, entity, setContaminated = false)

			for (gene in BlackDeathRecipe.getRequiredGenes(ClientUtil.localRegistryAccess!!)) {
				SyringeItem.addGene(syringeStack, gene)
			}

			val type = if (isMetal) "/metal" else ""

			return VirusJeiRecipe(
				id = GeneticsResequenced.modResource("/brewing/black_death$type"),
				ingredient = Ingredient.of(syringeStack),
				input = Ingredient.of(BrewingRecipes.viralAgentsPotionStack),
				output = DnaHelixItem.getHelixStack(ModGenes.BLACK_DEATH, ClientUtil.localRegistryAccess!!),
				tooltips = listOf(ModRecipeLang.BLACK_DEATH.toComponent().withStyle(ChatFormatting.GRAY))
			)
		}
	}
}
