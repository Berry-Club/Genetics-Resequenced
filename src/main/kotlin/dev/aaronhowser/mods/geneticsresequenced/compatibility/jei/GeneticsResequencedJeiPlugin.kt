package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.recipes.*
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
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
import net.minecraft.world.Container
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.Block
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
        val BLOOD_PURIFIER_TYPE: RecipeType<BloodPurifierRecipe> =
            RecipeType(BloodPurifierRecipeCategory.UID, BloodPurifierRecipe::class.java)
    }

    override fun getPluginUid(): ResourceLocation =
        OtherUtil.modResource("jei_plugin")

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        registration.addRecipeCategories(
            CellAnalyzerRecipeCategory(registration.jeiHelpers.guiHelper),
            MobToGeneRecipeCategory(registration.jeiHelpers.guiHelper),
            DnaExtractorRecipeCategory(registration.jeiHelpers.guiHelper),
            PlasmidInfuserRecipeCategory(registration.jeiHelpers.guiHelper),
            BloodPurifierRecipeCategory(registration.jeiHelpers.guiHelper)
        )
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {

        val blockRecipeTypeMap: Map<Block, RecipeType<out Recipe<Container>>> = mapOf(
            ModBlocks.CELL_ANALYZER to CellAnalyzerRecipeCategory.recipeType,
            ModBlocks.DNA_DECRYPTOR to MobToGeneRecipeCategory.recipeType,
            ModBlocks.DNA_EXTRACTOR to DnaExtractorRecipeCategory.recipeType,
            ModBlocks.PLASMID_INFUSER to PlasmidInfuserRecipeCategory.recipeType,
            ModBlocks.BLOOD_PURIFIER to BloodPurifierRecipeCategory.recipeType
        )

        for ((block, recipeType) in blockRecipeTypeMap) {
            registration.addRecipeCatalyst(
                ItemStack(block),
                recipeType
            )
        }

    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registration.addRecipes(CellAnalyzerRecipeCategory.recipeType, CellAnalyzerRecipe.getAllRecipes())
        registration.addRecipes(MobToGeneRecipeCategory.recipeType, MobToGeneRecipe.getAllRecipes())
        registration.addRecipes(DnaExtractorRecipeCategory.recipeType, DnaExtractorRecipe.getAllRecipes())
        registration.addRecipes(PlasmidInfuserRecipeCategory.recipeType, PlasmidInfuserRecipe.getAllRecipes())
        registration.addRecipes(BloodPurifierRecipeCategory.recipeType, BloodPurifierRecipe.getAllRecipes())

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