package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.VirusRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class VirusEmiRecipe(
    val inputDnaGene: Gene,
    val outputGene: Gene
) : AbstractEmiBrewingRecipe() {

    companion object {
        fun getAllVirusRecipes(): List<VirusEmiRecipe> {
            val allRegularGmoRecipes = BrewingRecipes.allRecipes.filterIsInstance<VirusRecipe>()

            return allRegularGmoRecipes.map {
                VirusEmiRecipe(
                    it.inputDnaGene,
                    it.outputGene
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
        val helixStack = ModItems.DNA_HELIX.toStack()
        DnaHelixItem.setGene(helixStack, inputDnaGene)
        ingredient = EmiIngredient.of(Ingredient.of(helixStack))

        val outputStack = ModItems.DNA_HELIX.toStack()
        DnaHelixItem.setGene(outputStack, outputGene)
        output = EmiStack.of(outputStack)
    }

    override fun getId(): ResourceLocation {
        val inputGeneString = inputDnaGene.id.toString().replace(':', '/')
        val outputGeneString = outputGene.id.toString().replace(':', '/')

        return OtherUtil.modResource("/virus/$inputGeneString/$outputGeneString")
    }

}