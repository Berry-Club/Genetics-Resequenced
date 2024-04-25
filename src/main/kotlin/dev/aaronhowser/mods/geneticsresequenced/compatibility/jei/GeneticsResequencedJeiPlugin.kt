package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.recipes.CellAnalyzerRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.DnaExtractorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.MobToGeneRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipes.PlasmidInfuserRecipe
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
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.ItemStack
import net.minecraftforge.registries.ForgeRegistries

@JeiPlugin
class GeneticsResequencedJeiPlugin : IModPlugin {

    companion object {
        val CELL_ANALYZER_TYPE: RecipeType<CellAnalyzerRecipe> =
            RecipeType(CellAnalyzerRecipeCategory.UID, CellAnalyzerRecipe::class.java)
        val DNA_EXTRACTOR_TYPE: RecipeType<DnaExtractorRecipe> =
            RecipeType(DnaExtractorRecipeCategory.UID, DnaExtractorRecipe::class.java)
        val MOB_TO_GENE_TYPE: RecipeType<MobToGeneRecipe> =
            RecipeType(MobToGeneRecipeCategory.UID, MobToGeneRecipe::class.java)
        val PLASMID_INFUSER_TYPE: RecipeType<PlasmidInfuserRecipe> =
            RecipeType(PlasmidInfuserRecipeCategory.UID, PlasmidInfuserRecipe::class.java)
    }

    override fun getPluginUid(): ResourceLocation =
        ResourceLocation(GeneticsResequenced.ID, "jei_plugin")

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        registration.addRecipeCategories(
            CellAnalyzerRecipeCategory(registration.jeiHelpers.guiHelper),
            MobToGeneRecipeCategory(registration.jeiHelpers.guiHelper),
            DnaExtractorRecipeCategory(registration.jeiHelpers.guiHelper),
            PlasmidInfuserRecipeCategory(registration.jeiHelpers.guiHelper)
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
        registration.addRecipeCatalyst(
            ItemStack(ModBlocks.PLASMID_INFUSER),
            PlasmidInfuserRecipeCategory.recipeType
        )
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(CellAnalyzerRecipeCategory.recipeType, CellAnalyzerRecipe.getAllRecipes())
        registration.addRecipes(MobToGeneRecipeCategory.recipeType, MobToGeneRecipe.getAllRecipes())
        registration.addRecipes(DnaExtractorRecipeCategory.recipeType, DnaExtractorRecipe.getAllRecipes())
        registration.addRecipes(PlasmidInfuserRecipeCategory.recipeType, PlasmidInfuserRecipe.getAllRecipes())

        mobGeneInformationRecipes(registration)
        organicMatterInformationRecipes(registration)
    }

    private fun organicMatterInformationRecipes(registration: IRecipeRegistration) {
        val allEntityTypes = ForgeRegistries.ENTITY_TYPES.values.filter { it.category != MobCategory.MISC }
        for (entityType in allEntityTypes) {
            val informationTextComponent = Component
                .translatable("info.geneticsresequenced.organic_matter", entityType.description)

            val organicMatterStack = ItemStack(ModItems.ORGANIC_MATTER).setMob(entityType)
                ?: throw IllegalStateException("Failed to create ItemStack for Organic Matter")

            registration.addIngredientInfo(
                organicMatterStack,
                VanillaTypes.ITEM_STACK,
                informationTextComponent
            )
        }

    }

    private fun mobGeneInformationRecipes(registration: IRecipeRegistration) {
        val allMobGenePairs = MobGenesRegistry.getRegistry().entries
        for ((entityType, genes) in allMobGenePairs) {
            val informationTextComponent =
                Component.translatable("info.geneticsresequenced.mob_gene.line1", entityType.description)

            val sumOfWeights = genes.values.sum()

            for ((gene, weight) in genes) {
                val chance = (weight.toDouble() / sumOfWeights.toDouble() * 100).toInt()

                //FIXME: doesn't work
                val component = Component.translatable(
                    "info.geneticsresequenced.mob_gene.line2",
                    chance,
                    gene?.nameComponent ?: Gene.basicGeneComponent
                )

                informationTextComponent.append(component)
            }

            registration.addIngredientInfo(
                ItemStack(ModItems.DNA_HELIX).setMob(entityType)!!,
                VanillaTypes.ITEM_STACK,
                informationTextComponent
            )
        }
    }

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        registration.registerSubtypeInterpreter(ModItems.PLASMID) { stack, _ ->
            val gene: Gene? = stack.getGene()
            gene?.id?.toString() ?: "no_gene"
        }
    }

}