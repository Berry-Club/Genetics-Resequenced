package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.VirusRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
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

    override fun getId(): ResourceLocation {
        val inputGeneString = inputDnaGeneRk.location().toString().replace(':', '/')
        val outputGeneString = outputGeneRk.location().toString().replace(':', '/')

        return OtherUtil.modResource("/virus/$inputGeneString/$outputGeneString")
    }

}