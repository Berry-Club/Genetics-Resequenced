package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi

import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.AntiPlasmidEmiRecipes
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing.BlackDeathEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing.GmoEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.blood_purifier.PurifySyringeEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.cell_analyzer.OrganicMatterToCellEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.dna_decryptor.DecryptHelixEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.dna_extractor.CellToHelixEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.plasmid_infuser.PlasmidInfuserEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.plasmid_injector.PlasmidInjectorEmiRecipe
import dev.aaronhowser.mods.geneticsresequenced.data.MobGeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.PlasmidProgressItemComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
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

        val PLASMID_INFUSER_STACK: EmiStack = EmiStack.of(ModBlocks.PLASMID_INFUSER)
        val PLASMID_INFUSER_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("plasmid_infuser"), PLASMID_INFUSER_STACK)

        val PLASMID_INJECTOR_STACK: EmiStack = EmiStack.of(ModBlocks.PLASMID_INJECTOR)
        val PLASMID_INJECTOR_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("plasmid_injector"), PLASMID_INJECTOR_STACK)

        val INCUBATOR_STACK: EmiStack = EmiStack.of(ModBlocks.INCUBATOR)
        val ADVANCED_INCUBATOR_STACK: EmiStack = EmiStack.of(ModBlocks.ADVANCED_INCUBATOR)

        val GMO_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("gmo"), ADVANCED_INCUBATOR_STACK)
        val CELL_GROWTH_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("cell_growth"), ADVANCED_INCUBATOR_STACK)
        val SUBSTRATE_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("substrate"), ADVANCED_INCUBATOR_STACK)
        val VIRUS_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("virus"), ADVANCED_INCUBATOR_STACK)

        val ORGANIC_MATTER_STACK: EmiStack = EmiStack.of(ModItems.ORGANIC_MATTER)
        val CELL_STACK: EmiStack = EmiStack.of(ModItems.CELL)
        val GMO_CELL_STACK: EmiStack = EmiStack.of(ModItems.GMO_CELL)
        val DNA_HELIX_STACK: EmiStack = EmiStack.of(ModItems.DNA_HELIX)
        val PLASMID_STACK: EmiStack = EmiStack.of(ModItems.PLASMID)
        val ANTI_PLASMID_STACK: EmiStack = EmiStack.of(ModItems.ANTI_PLASMID)
    }

    override fun register(registry: EmiRegistry) {
        bloodPurifier(registry)
        cellAnalyzer(registry)
        dnaExtractor(registry)
        dnaDecryptor(registry)
        plasmidInfuser(registry)
        plasmidInjector(registry)

        potions(registry)

        AntiPlasmidEmiRecipes.setAntiPlasmidRecipes(registry)
        AntiPlasmidEmiRecipes.unsetAntiPlasmidRecipes(registry)

        comparisons(registry)
    }

    private fun comparisons(registry: EmiRegistry) {
        val entityTypeComparison = Comparison.compareData { it.get(EntityTypeItemComponent.component) }
        val plasmidProgressComparison = Comparison.compareData { it.get(PlasmidProgressItemComponent.component) }

        registry.setDefaultComparison(CELL_STACK, entityTypeComparison)
        registry.setDefaultComparison(GMO_CELL_STACK, Comparison.compareComponents())
        registry.setDefaultComparison(DNA_HELIX_STACK, Comparison.compareComponents())
        registry.setDefaultComparison(PLASMID_STACK, plasmidProgressComparison)
        registry.setDefaultComparison(ANTI_PLASMID_STACK, plasmidProgressComparison)
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

    private fun plasmidInfuser(registry: EmiRegistry) {
        registry.addCategory(PLASMID_INFUSER_CATEGORY)
        registry.addWorkstation(PLASMID_INFUSER_CATEGORY, PLASMID_INFUSER_STACK)

        for (gene in GeneRegistry.GENE_REGISTRY.filterNot { it.isHidden }) {
            registry.addRecipe(PlasmidInfuserEmiRecipe(gene, basic = true))
            registry.addRecipe(PlasmidInfuserEmiRecipe(gene, basic = false))
        }
    }

    private fun plasmidInjector(registry: EmiRegistry) {
        registry.addCategory(PLASMID_INJECTOR_CATEGORY)
        registry.addWorkstation(PLASMID_INJECTOR_CATEGORY, PLASMID_INJECTOR_STACK)

        val addingGlass: MutableList<PlasmidInjectorEmiRecipe> = mutableListOf()
        val addingMetal: MutableList<PlasmidInjectorEmiRecipe> = mutableListOf()
        val removingGlass: MutableList<PlasmidInjectorEmiRecipe> = mutableListOf()
        val removingMetal: MutableList<PlasmidInjectorEmiRecipe> = mutableListOf()

        for (gene in GeneRegistry.GENE_REGISTRY.filterNot { it.isHidden }) {
            addingGlass.add(PlasmidInjectorEmiRecipe(gene, isMetal = false, isAntiPlasmid = false))
            addingMetal.add(PlasmidInjectorEmiRecipe(gene, isMetal = false, isAntiPlasmid = true))
            removingGlass.add(PlasmidInjectorEmiRecipe(gene, isMetal = true, isAntiPlasmid = false))
            removingMetal.add(PlasmidInjectorEmiRecipe(gene, isMetal = true, isAntiPlasmid = true))
        }

        for (recipe in addingGlass + addingMetal + removingGlass + removingMetal) {
            registry.addRecipe(recipe)
        }
    }

    private fun potions(registry: EmiRegistry) {
        registry.addWorkstation(VanillaEmiRecipeCategories.BREWING, INCUBATOR_STACK)
        registry.addWorkstation(VanillaEmiRecipeCategories.BREWING, ADVANCED_INCUBATOR_STACK)

        registry.addRecipe(BlackDeathEmiRecipe(false))
        registry.addRecipe(BlackDeathEmiRecipe(true))

        advancedIncubator(registry)
    }

    private fun advancedIncubator(registry: EmiRegistry) {
        registry.addCategory(GMO_CATEGORY)
        registry.addWorkstation(GMO_CATEGORY, ADVANCED_INCUBATOR_STACK)

        for (recipe in GmoEmiRecipe.getGmoRecipes()) {
            registry.addRecipe(recipe)
        }

        registry.addCategory(CELL_GROWTH_CATEGORY)
        registry.addWorkstation(CELL_GROWTH_CATEGORY, ADVANCED_INCUBATOR_STACK)


        registry.addCategory(SUBSTRATE_CATEGORY)
        registry.addWorkstation(SUBSTRATE_CATEGORY, ADVANCED_INCUBATOR_STACK)


        registry.addCategory(VIRUS_CATEGORY)
        registry.addWorkstation(VIRUS_CATEGORY, ADVANCED_INCUBATOR_STACK)
    }

}