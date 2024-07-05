package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe

import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.world.item.crafting.Ingredient

object AntiPlasmidEmiRecipes {

    fun setAntiPlasmidRecipes(registry: EmiRegistry) {
        val emptyAntiPlasmid: EmiIngredient = EmiIngredient.of(Ingredient.of(ModItems.ANTI_PLASMID))

        for (gene in GeneRegistry.getRegistrySorted().filterNot { it.isHidden }) {
            val plasmidStack = ModItems.PLASMID.toStack()
            PlasmidItem.setGene(plasmidStack, gene, gene.dnaPointsRequired)

            val setAntiPlasmid = ModItems.ANTI_PLASMID.toStack()
            PlasmidItem.setGene(setAntiPlasmid, gene, gene.dnaPointsRequired)

            val geneString = gene.id.toString().replace(':', '/')

            registry.addRecipe(
                EmiCraftingRecipe(
                    listOf(emptyAntiPlasmid, EmiIngredient.of(Ingredient.of(plasmidStack))),
                    EmiStack.of(setAntiPlasmid),
                    OtherUtil.modResource("/set_anti_plasmid/${geneString}"),
                    true
                )
            )
        }
    }

    fun unsetAntiPlasmidRecipes(registry: EmiRegistry) {
        for (gene in GeneRegistry.getRegistrySorted().filterNot { it.isHidden }) {
            val antiPlasmidStack = ModItems.ANTI_PLASMID.toStack()
            PlasmidItem.setGene(antiPlasmidStack, gene, gene.dnaPointsRequired)

            val geneString = gene.id.toString().replace(':', '/')

            registry.addRecipe(
                EmiCraftingRecipe(
                    listOf(EmiIngredient.of(Ingredient.of(antiPlasmidStack))),
                    EmiStack.of(ModItems.ANTI_PLASMID),
                    OtherUtil.modResource("/unset_anti_plasmid/${geneString}"),
                    true
                )
            )
        }

    }

}