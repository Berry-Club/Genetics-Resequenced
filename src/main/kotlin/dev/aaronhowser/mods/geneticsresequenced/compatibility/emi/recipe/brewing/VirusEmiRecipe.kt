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
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class VirusEmiRecipe(
    val inputDnaGene: Holder<Gene>,
    val outputGene: Holder<Gene>
) : AbstractEmiBrewingRecipe() {

    companion object {
        fun getAllRecipes(): List<VirusEmiRecipe> {
            val allRegularVirusRecipes = BrewingRecipes.allRecipes.filterIsInstance<VirusRecipe>()

            return allRegularVirusRecipes.map {
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
        DnaHelixItem.setGeneRk(helixStack, inputDnaGene)
        ingredient = EmiIngredient.of(Ingredient.of(helixStack))

        val outputStack = ModItems.DNA_HELIX.toStack()
        DnaHelixItem.setGeneRk(outputStack, outputGene)
        output = EmiStack.of(outputStack)
    }

    override fun getId(): ResourceLocation {
        val inputGeneString = inputDnaGene.value().id.toString().replace(':', '/')
        val outputGeneString = outputGene.value().id.toString().replace(':', '/')

        return OtherUtil.modResource("/virus/$inputGeneString/$outputGeneString")
    }

}