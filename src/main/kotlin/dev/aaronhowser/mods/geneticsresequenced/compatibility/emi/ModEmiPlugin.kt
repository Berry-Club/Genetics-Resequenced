package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.blood_purifier.PurifySyringeEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.cell_analyzer.OrganicMatterToCellEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.dna_decryptor.DecryptHelixEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.dna_extractor.CellToHelixEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.data.MobGeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiStack

@EmiEntrypoint
class ModEmiPlugin : EmiPlugin {

    companion object {
        val BLOOD_PURIFIER_STACK: EmiStack = EmiStack.of(ModBlocks.BLOOD_PURIFIER)
        val BLOOD_PURIFIER_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("blood_purifier"), BLOOD_PURIFIER_STACK)

        val CELL_ANALYZER_STACK: EmiStack = EmiStack.of(ModBlocks.CELL_ANALYZER)
        val CELL_ANALYZER_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("cell_analyzer"), CELL_ANALYZER_STACK)

        val DNA_EXTRACTOR_STACK: EmiStack = EmiStack.of(ModBlocks.DNA_EXTRACTOR)
        val DNA_EXTRACTOR_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("dna_extractor"), DNA_EXTRACTOR_STACK)

        val DNA_DECRYPTOR_STACK: EmiStack = EmiStack.of(ModBlocks.DNA_DECRYPTOR)
        val DNA_DECRYPTOR_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("dna_decryptor"), DNA_DECRYPTOR_STACK)

        val ORGANIC_MATTER_STACK: EmiStack = EmiStack.of(ModItems.ORGANIC_MATTER)
        val CELL_STACK: EmiStack = EmiStack.of(ModItems.CELL)
        val DNA_HELIX_STACK: EmiStack = EmiStack.of(ModItems.DNA_HELIX)
    }

    override fun register(registry: EmiRegistry) {
        bloodPurifier(registry)
        cellAnalyzer(registry)
        dnaExtractor(registry)
        dnaDecryptor(registry)

        comparisons(registry)
    }

    private fun comparisons(registry: EmiRegistry) {
        val geneComparison = Comparison.compareData { it.get(GeneItemComponent.component) }
        val entityTypeComparison = Comparison.compareData { it.get(EntityTypeItemComponent.component) }

        registry.setDefaultComparison(ORGANIC_MATTER_STACK, entityTypeComparison)
        registry.setDefaultComparison(CELL_STACK, entityTypeComparison)
        registry.setDefaultComparison(DNA_HELIX_STACK, geneComparison)
//        registry.setDefaultComparison(DNA_HELIX_STACK, entityTypeComparison)
    }

    private fun bloodPurifier(registry: EmiRegistry) {
        registry.addCategory(BLOOD_PURIFIER_CATEGORY)
        registry.addWorkstation(BLOOD_PURIFIER_CATEGORY, BLOOD_PURIFIER_STACK)

        registry.addRecipe(PurifySyringeEmiRecipe(isMetal = false))
        registry.addRecipe(PurifySyringeEmiRecipe(isMetal = true))
    }

    private fun cellAnalyzer(registry: EmiRegistry) {
        registry.addCategory(CELL_ANALYZER_CATEGORY)
        registry.addWorkstation(CELL_ANALYZER_CATEGORY, CELL_ANALYZER_STACK)

        for (entityType in EntityDnaItem.validEntityTypes) {
            registry.addRecipe(OrganicMatterToCellEmiRecipe(entityType))
        }
    }

    private fun dnaExtractor(registry: EmiRegistry) {
        registry.addCategory(DNA_EXTRACTOR_CATEGORY)
        registry.addWorkstation(DNA_EXTRACTOR_CATEGORY, DNA_EXTRACTOR_STACK)

        for (entityType in EntityDnaItem.validEntityTypes) {
            registry.addRecipe(CellToHelixEmiRecipe(entityType))
        }
    }

    private fun dnaDecryptor(registry: EmiRegistry) {
        registry.addCategory(DNA_DECRYPTOR_CATEGORY)
        registry.addWorkstation(DNA_DECRYPTOR_CATEGORY, DNA_DECRYPTOR_STACK)

        for ((entityType, map) in MobGeneRegistry.getRegistry()) {
            val totalWeight = map.values.sum()

            for ((gene, weight) in map) {
                val chance = weight.toFloat() / totalWeight

                registry.addRecipe(DecryptHelixEmiRecipe(entityType, gene, chance))
            }
        }
    }

}