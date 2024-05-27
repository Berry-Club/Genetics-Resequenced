package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.*
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.incubator.CellGrowthRecipeCategory
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.incubator.GmoRecipeCategory
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.incubator.SubstrateCellRecipeCategory
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.recipes.*
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei.CellGrowthRecipePage
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei.GmoRecipePage
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei.SubstrateCellRecipePage
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

@JeiPlugin
class GeneticsResequencedJeiPlugin : IModPlugin {

    companion object {
        val CELL_ANALYZER_TYPE: RecipeType<CellAnalyzerRecipe> =
            RecipeType(CellAnalyzerRecipeCategory.UID, CellAnalyzerRecipe::class.java)
        val DNA_EXTRACTOR_TYPE: RecipeType<DnaExtractorRecipe> =
            RecipeType(DnaExtractorRecipeCategory.UID, DnaExtractorRecipe::class.java)
        val DNA_DECRYPTOR_TYPE: RecipeType<DnaDecryptorRecipe> =
            RecipeType(DnaDecryptorRecipeCategory.UID, DnaDecryptorRecipe::class.java)
        val PLASMID_INFUSER_TYPE: RecipeType<PlasmidInfuserRecipe> =
            RecipeType(PlasmidInfuserRecipeCategory.UID, PlasmidInfuserRecipe::class.java)
        val BLOOD_PURIFIER_TYPE: RecipeType<BloodPurifierRecipe> =
            RecipeType(BloodPurifierRecipeCategory.UID, BloodPurifierRecipe::class.java)

        val GMO_RECIPE_TYPE: RecipeType<GmoRecipePage> =
            RecipeType(GmoRecipeCategory.UID, GmoRecipePage::class.java)
        val CELL_GROWTH_RECIPE_TYPE: RecipeType<CellGrowthRecipePage> =
            RecipeType(CellGrowthRecipeCategory.UID, CellGrowthRecipePage::class.java)
        val SUBSTRATE_CELL_RECIPE_TYPE: RecipeType<SubstrateCellRecipePage> =
            RecipeType(SubstrateCellRecipeCategory.UID, SubstrateCellRecipePage::class.java)
    }

    override fun getPluginUid(): ResourceLocation =
        OtherUtil.modResource("jei_plugin")

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper = registration.jeiHelpers.guiHelper

        registration.addRecipeCategories(
            CellAnalyzerRecipeCategory(guiHelper),
            DnaDecryptorRecipeCategory(guiHelper),
            DnaExtractorRecipeCategory(guiHelper),
            PlasmidInfuserRecipeCategory(guiHelper),
            BloodPurifierRecipeCategory(guiHelper),
            GmoRecipeCategory(guiHelper),
            CellGrowthRecipeCategory(guiHelper),
            SubstrateCellRecipeCategory(guiHelper)
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {

        val blockRecipeTypeMap: MutableList<Pair<Block, RecipeType<out Recipe<Container>>>> = mutableListOf(
            ModBlocks.CELL_ANALYZER.get() to CellAnalyzerRecipeCategory.recipeType,
            ModBlocks.DNA_DECRYPTOR.get() to DnaDecryptorRecipeCategory.recipeType,
            ModBlocks.DNA_EXTRACTOR.get() to DnaExtractorRecipeCategory.recipeType,
            ModBlocks.PLASMID_INFUSER.get() to PlasmidInfuserRecipeCategory.recipeType,
            ModBlocks.BLOOD_PURIFIER.get() to BloodPurifierRecipeCategory.recipeType,
        )

        fun addIncubatorRecipeType(recipeType: RecipeType<out Recipe<Container>>) {
            blockRecipeTypeMap.add(ModBlocks.INCUBATOR.get() to recipeType)
            blockRecipeTypeMap.add(Blocks.BREWING_STAND to recipeType)
        }

        addIncubatorRecipeType(GmoRecipeCategory.recipeType)
        addIncubatorRecipeType(CellGrowthRecipeCategory.recipeType)
        addIncubatorRecipeType(SubstrateCellRecipeCategory.recipeType)

        for ((block, recipeType) in blockRecipeTypeMap) {
            registration.addRecipeCatalyst(
                ItemStack(block),
                recipeType
            )
        }

        registration.addRecipeCatalyst(
            ItemStack(ModBlocks.INCUBATOR.get()),
            RecipeTypes.BREWING
        )

    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(CellAnalyzerRecipeCategory.recipeType, CellAnalyzerRecipe.getAllRecipes())
        registration.addRecipes(DnaDecryptorRecipeCategory.recipeType, DnaDecryptorRecipe.getAllRecipes())
        registration.addRecipes(DnaExtractorRecipeCategory.recipeType, DnaExtractorRecipe.getAllRecipes())
        registration.addRecipes(PlasmidInfuserRecipeCategory.recipeType, PlasmidInfuserRecipe.getAllRecipes())
        registration.addRecipes(BloodPurifierRecipeCategory.recipeType, BloodPurifierRecipe.getAllRecipes())

        registration.addRecipes(GmoRecipeCategory.recipeType, GmoRecipePage.getAllRecipes())
        registration.addRecipes(CellGrowthRecipeCategory.recipeType, CellGrowthRecipePage.getAllRecipes())
        registration.addRecipes(SubstrateCellRecipeCategory.recipeType, SubstrateCellRecipePage.getAllRecipes())

        InformationRecipes.organicMatter(registration)
        InformationRecipes.mobGenes(registration)
        InformationRecipes.requiredGenes(registration)
    }

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {

        registration.useNbtForSubtypes(
            ModItems.PLASMID.get(),
            ModItems.DNA_HELIX.get(),
            ModItems.GMO_DNA_HELIX.get(),
            ModItems.GMO_CELL.get()
        )

    }

}