package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.recipes.CellAnalyzerRecipe
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

@JeiPlugin
class GeneticsResequencedJeiPlugin : IModPlugin {

    companion object {
        val CELL_ANALYZER_TYPE: RecipeType<CellAnalyzerRecipe> =
            RecipeType(CellAnalyzerRecipeCategory.UID, CellAnalyzerRecipe::class.java)
    }

    override fun getPluginUid(): ResourceLocation =
        ResourceLocation(GeneticsResequenced.ID, "jei_plugin")

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        registration.addRecipeCategories(
            CellAnalyzerRecipeCategory(registration.jeiHelpers.guiHelper)
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(
            ItemStack(ModBlocks.CELL_ANALYZER),
            CellAnalyzerRecipe.JEI_RECIPE_TYPE
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(CellAnalyzerRecipe.JEI_RECIPE_TYPE, CellAnalyzerRecipe.getAllRecipes())
    }

}