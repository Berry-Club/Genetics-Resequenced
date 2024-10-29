package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.AntiPlasmidEmiRecipes
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.ModInformationRecipes
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.*
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.incubator.*
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
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

        val PLASMID_INFUSER_STACK: EmiStack = EmiStack.of(ModBlocks.PLASMID_INFUSER)
        val PLASMID_INFUSER_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("plasmid_infuser"), PLASMID_INFUSER_STACK)

        val PLASMID_INJECTOR_STACK: EmiStack = EmiStack.of(ModBlocks.PLASMID_INJECTOR)
        val PLASMID_INJECTOR_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("plasmid_injector"), PLASMID_INJECTOR_STACK)

        val INCUBATOR_STACK: EmiStack = EmiStack.of(ModBlocks.INCUBATOR)
        val INCUBATOR_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("incubator"), INCUBATOR_STACK)

        val ADVANCED_INCUBATOR_STACK: EmiStack = EmiStack.of(ModBlocks.ADVANCED_INCUBATOR)

        val ORGANIC_MATTER_STACK: EmiStack = EmiStack.of(ModItems.ORGANIC_MATTER)
        val CELL_STACK: EmiStack = EmiStack.of(ModItems.CELL)
        val GMO_CELL_STACK: EmiStack = EmiStack.of(ModItems.GMO_CELL)
        val DNA_HELIX_STACK: EmiStack = EmiStack.of(ModItems.DNA_HELIX)
        val PLASMID_STACK: EmiStack = EmiStack.of(ModItems.PLASMID)
        val ANTI_PLASMID_STACK: EmiStack = EmiStack.of(ModItems.ANTI_PLASMID)

        val GMO_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("gmo"), GMO_CELL_STACK)
        val SET_ENTITY_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("set_entity"), EmiStack.of(BrewingRecipes.cellGrowthPotionStack))
        val CELL_DUPE_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("cell_dupe"), CELL_STACK)
        val VIRUS_CATEGORY: EmiRecipeCategory =
            EmiRecipeCategory(OtherUtil.modResource("virus"), EmiStack.of(BrewingRecipes.viralAgentsPotionStack))

    }

    override fun register(registry: EmiRegistry) {
        bloodPurifier(registry)
        cellAnalyzer(registry)
        dnaExtractor(registry)
        dnaDecryptor(registry)
        plasmidInfuser(registry)
        plasmidInjector(registry)
        incubator(registry)

        AntiPlasmidEmiRecipes.setAntiPlasmidRecipes(registry)
        AntiPlasmidEmiRecipes.unsetAntiPlasmidRecipes(registry)

        for (infoRecipe in ModInformationRecipes.getInformationRecipes(ClientUtil.localRegistryAccess!!)) {
            registry.addRecipe(infoRecipe)
        }

        setComparisons(registry)
    }

    private fun setComparisons(registry: EmiRegistry) {
        val plasmidProgressComparison =
            Comparison.compareData { it.get(ModDataComponents.PLASMID_PROGRESS_COMPONENT.get()) }

        registry.setDefaultComparison(CELL_STACK, Comparison.compareComponents())
        registry.setDefaultComparison(GMO_CELL_STACK, Comparison.compareComponents())
        registry.setDefaultComparison(DNA_HELIX_STACK, Comparison.compareComponents())
        registry.setDefaultComparison(ORGANIC_MATTER_STACK, Comparison.compareComponents())
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

        for (recipe in OrganicMatterToCellEmiRecipe.getAllRecipes()) {
            registry.addRecipe(recipe)
        }
    }

    private fun dnaExtractor(registry: EmiRegistry) {
        registry.addCategory(DNA_EXTRACTOR_CATEGORY)
        registry.addWorkstation(DNA_EXTRACTOR_CATEGORY, DNA_EXTRACTOR_STACK)

        for (recipe in CellToHelixEmiRecipe.getAllRecipes(registry.recipeManager)) {
            registry.addRecipe(recipe)
        }
    }

    private fun dnaDecryptor(registry: EmiRegistry) {
        registry.addCategory(DNA_DECRYPTOR_CATEGORY)
        registry.addWorkstation(DNA_DECRYPTOR_CATEGORY, DNA_DECRYPTOR_STACK)

        for (recipe in DecryptHelixEmiRecipe.getAllRecipes()) {
            registry.addRecipe(recipe)
        }
    }

    private fun plasmidInfuser(registry: EmiRegistry) {
        registry.addCategory(PLASMID_INFUSER_CATEGORY)
        registry.addWorkstation(PLASMID_INFUSER_CATEGORY, PLASMID_INFUSER_STACK)

        for (recipe in PlasmidInfuserEmiRecipe.getAllRecipes()) {
            registry.addRecipe(recipe)
        }
    }

    private fun plasmidInjector(registry: EmiRegistry) {
        registry.addCategory(PLASMID_INJECTOR_CATEGORY)
        registry.addWorkstation(PLASMID_INJECTOR_CATEGORY, PLASMID_INJECTOR_STACK)

        for (recipe in PlasmidInjectorEmiRecipe.getAllRecipes()) {
            registry.addRecipe(recipe)
        }
    }

    private fun incubator(registry: EmiRegistry) {
        basicIncubatorRecipes(registry)
        substrate(registry)
        setPotionEntity(registry)
        virus(registry)
        gmo(registry)

        for (recipe in BlackDeathEmiRecipe.getAllRecipes()) {
            registry.addRecipe(recipe)
        }
    }

    private fun basicIncubatorRecipes(registry: EmiRegistry) {
        registry.addCategory(INCUBATOR_CATEGORY)
        registry.addWorkstation(INCUBATOR_CATEGORY, INCUBATOR_STACK)
        registry.addWorkstation(INCUBATOR_CATEGORY, ADVANCED_INCUBATOR_STACK)

        for (recipe in BasicIncubatorEmiRecipe.getAllRecipes(registry.recipeManager)) {
            registry.addRecipe(recipe)
        }
    }

    private fun substrate(registry: EmiRegistry) {
        registry.addCategory(CELL_DUPE_CATEGORY)
        registry.addWorkstation(CELL_DUPE_CATEGORY, INCUBATOR_STACK)
        registry.addWorkstation(CELL_DUPE_CATEGORY, ADVANCED_INCUBATOR_STACK)

        for (recipe in CellDupeEmiRecipe.getAllRecipes(registry.recipeManager)) {
            registry.addRecipe(recipe)
        }
    }

    private fun setPotionEntity(registry: EmiRegistry) {
        registry.addCategory(SET_ENTITY_CATEGORY)
        registry.addWorkstation(SET_ENTITY_CATEGORY, INCUBATOR_STACK)
        registry.addWorkstation(SET_ENTITY_CATEGORY, ADVANCED_INCUBATOR_STACK)

        for (recipe in SetPotionEntityEmiRecipe.getAllRecipes()) {
            registry.addRecipe(recipe)
        }
    }

    private fun virus(registry: EmiRegistry) {
        registry.addCategory(VIRUS_CATEGORY)
        registry.addWorkstation(VIRUS_CATEGORY, INCUBATOR_STACK)
        registry.addWorkstation(VIRUS_CATEGORY, ADVANCED_INCUBATOR_STACK)

        for (recipe in VirusEmiRecipe.getAllRecipes(registry.recipeManager)) {
            registry.addRecipe(recipe)
        }
    }

    private fun gmo(registry: EmiRegistry) {
        registry.addCategory(GMO_CATEGORY)
        registry.addWorkstation(GMO_CATEGORY, ADVANCED_INCUBATOR_STACK)

        for (recipe in GmoEmiRecipe.getAllRecipes(registry.recipeManager)) {
            registry.addRecipe(recipe)
        }
    }

}