package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.recipes.CellAnalyzerRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.DnaExtractorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.MobToGeneRecipe
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

@JeiPlugin
class GeneticsResequencedJeiPlugin : IModPlugin {

    companion object {
        val CELL_ANALYZER_TYPE: RecipeType<CellAnalyzerRecipe> =
            RecipeType(CellAnalyzerRecipeCategory.UID, CellAnalyzerRecipe::class.java)
        val DNA_EXTRACTOR_TYPE: RecipeType<DnaExtractorRecipe> =
            RecipeType(DnaExtractorRecipeCategory.UID, DnaExtractorRecipe::class.java)
        val MOB_TO_GENE_TYPE: RecipeType<MobToGeneRecipe> =
            RecipeType(MobToGeneRecipeCategory.UID, MobToGeneRecipe::class.java)
    }

    override fun getPluginUid(): ResourceLocation =
        ResourceLocation(GeneticsResequenced.ID, "jei_plugin")

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        registration.addRecipeCategories(
            CellAnalyzerRecipeCategory(registration.jeiHelpers.guiHelper),
            MobToGeneRecipeCategory(registration.jeiHelpers.guiHelper),
            DnaExtractorRecipeCategory(registration.jeiHelpers.guiHelper)
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(
            ItemStack(ModBlocks.CELL_ANALYZER),
            CellAnalyzerRecipeCategory.recipeType
        )
        registration.addRecipeCatalyst(
            ItemStack(ModBlocks.DNA_DECRYPTOR),
            MobToGeneRecipeCategory.recipeType
        )
        registration.addRecipeCatalyst(
            ItemStack(ModBlocks.DNA_EXTRACTOR),
            DnaExtractorRecipeCategory.recipeType
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(CellAnalyzerRecipeCategory.recipeType, CellAnalyzerRecipe.getAllRecipes())
        registration.addRecipes(MobToGeneRecipeCategory.recipeType, MobToGeneRecipe.getAllRecipes())
        registration.addRecipes(DnaExtractorRecipeCategory.recipeType, DnaExtractorRecipe.getAllRecipes())

        mobGeneRecipes(registration)
    }

    private fun mobGeneRecipes(registration: IRecipeRegistration) {
        val allMobGenePairs = MobGenesRegistry.getRegistry().entries
        for ((entityType, genes) in allMobGenePairs) {
            val informationTextComponent =
                Component
                    .empty()
                    .append(entityType.description)
                    .append(Component.literal(" has these traits:\n"))

            for (gene in genes) {
                informationTextComponent.append(
                    Component.literal("\n- ").append(gene.nameComponent)
                )
            }

            registration.addIngredientInfo(
                ItemStack(ModItems.DNA_HELIX).setMob(entityType)!!,
                VanillaTypes.ITEM_STACK,
                informationTextComponent
            )
        }
    }

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        registration.registerSubtypeInterpreter(ModItems.DNA_HELIX) { stack, _ ->
            val gene: Gene? = DnaHelixItem.getGene(stack)
            gene?.id ?: "no_gene"
        }
    }

}