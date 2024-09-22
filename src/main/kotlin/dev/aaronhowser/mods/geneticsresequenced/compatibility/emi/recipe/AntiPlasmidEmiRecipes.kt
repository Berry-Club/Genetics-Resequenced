package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isHidden
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.world.item.crafting.Ingredient

object AntiPlasmidEmiRecipes {

    fun setAntiPlasmidRecipes(registry: EmiRegistry) {
        val emptyAntiPlasmid: EmiIngredient = EmiIngredient.of(Ingredient.of(ModItems.ANTI_PLASMID))

        val visibleGenes = GeneRegistry
            .getRegistrySorted(ClientUtil.localRegistryAccess!!)
            .filterNot { it.isHidden }

        for (geneHolder in visibleGenes) {
            val plasmidStack = ModItems.PLASMID.toStack()
            PlasmidItem.setGene(plasmidStack, geneHolder, geneHolder.value().dnaPointsRequired)

            val setAntiPlasmid = ModItems.ANTI_PLASMID.toStack()
            PlasmidItem.setGene(setAntiPlasmid, geneHolder, geneHolder.value().dnaPointsRequired)

            val geneString = geneHolder.key!!.location().toString().replace(':', '/')

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
        val visibleGenes = GeneRegistry
            .getRegistrySorted(ClientUtil.localRegistryAccess!!)
            .filterNot { it.isHidden }

        for (geneHolder in visibleGenes) {
            val antiPlasmidStack = ModItems.ANTI_PLASMID.toStack()
            PlasmidItem.setGene(antiPlasmidStack, geneHolder, geneHolder.value().dnaPointsRequired)

            val geneString = geneHolder.key!!.location().toString().replace(':', '/')

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