package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.PlasmidItem.Companion.getPlasmid
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.NonNullList
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapelessRecipe

object CustomRecipeHandler {

    fun replaceSpecialCraftingRecipes(): List<CraftingRecipe> {
        return setAntiPlasmidRecipes()
    }

    private fun setAntiPlasmidRecipes(): List<CraftingRecipe> {

        val group = GeneticsResequenced.ID + ".set_anti_plasmid"
        val allGenes = Gene.getRegistry()
        val emptyAntiPlasmid = ModItems.ANTI_PLASMID.itemStack

        val recipes = mutableListOf<CraftingRecipe>()

        for (gene in allGenes) {
            if (!gene.isActive) continue
            if (gene.hidden) continue

            val plasmid = gene.getPlasmid()
            val setAntiPlasmid = emptyAntiPlasmid.copy().setGene(gene)

            recipes.add(
                ShapelessRecipe(
                    OtherUtil.modResource("set_anti_plasmid/${gene.id.toString().replace(':', '/')}"),
                    group,
                    setAntiPlasmid,
                    NonNullList.of(
                        Ingredient.EMPTY,
                        Ingredient.of(plasmid),
                        Ingredient.of(emptyAntiPlasmid.copy())
                    )
                )
            )

        }

        return recipes
    }

}