package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.VirusRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class VirusEmiRecipe(
    val inputDnaGeneRk: ResourceKey<Gene>,
    val outputGeneRk: ResourceKey<Gene>
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
        DnaHelixItem.setGeneHolder(helixStack, inputDnaGeneRk.getHolder(GeneticsResequenced.registryAccess!!)!!)
        ingredient = EmiIngredient.of(Ingredient.of(helixStack))

        val outputStack = ModItems.DNA_HELIX.toStack()
        DnaHelixItem.setGeneHolder(outputStack, outputGeneRk.getHolder(GeneticsResequenced.registryAccess!!)!!)
        output = EmiStack.of(outputStack)
    }

    override fun getId(): ResourceLocation {
        val inputGeneString = inputDnaGeneRk.location().toString().replace(':', '/')
        val outputGeneString = outputGeneRk.location().toString().replace(':', '/')

        return OtherUtil.modResource("/virus/$inputGeneString/$outputGeneString")
    }

}