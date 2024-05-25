package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.*
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.incubator.CellGrowthRecipeCategory
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.incubator.GmoRecipeCategory
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.incubator.SubstrateCellRecipeCategory
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.recipes.*
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei.CellGrowthRecipePage
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei.GmoRecipePage
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei.SubstrateCellRecipePage
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.registries.ForgeRegistries

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

        mobGeneInformationRecipes(registration)
        organicMatterInformationRecipes(registration)
    }

    private fun organicMatterInformationRecipes(registration: IRecipeRegistration) {
        val allEntityTypes = ForgeRegistries.ENTITY_TYPES.values.filter { it.category != MobCategory.MISC }
        for (entityType in allEntityTypes) {
            val informationTextComponent = Component
                .translatable("info.geneticsresequenced.organic_matter", entityType.description)

            val organicMatterStack = ItemStack(ModItems.ORGANIC_MATTER.get()).setMob(entityType)
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

                val component = Component.translatable(
                    "info.geneticsresequenced.mob_gene.line2",
                    chance,
                    gene?.nameComponent ?: Gene.basicGeneComponent
                )

                informationTextComponent.append(component)
            }

            registration.addIngredientInfo(
                ItemStack(ModItems.DNA_HELIX.get()).setMob(entityType)!!,
                VanillaTypes.ITEM_STACK,
                informationTextComponent
            )
        }
    }

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {

        registration.registerSubtypeInterpreter(
            ModItems.ORGANIC_MATTER.get(),
            IIngredientSubtypeInterpreter<ItemStack> { stack, _ ->
                val entityType: EntityType<*>? = EntityDnaItem.getEntityType(stack)
                entityType?.toShortString() ?: "no_entity"
            })

        registration.registerSubtypeInterpreter(
            ModItems.CELL.get(),
            IIngredientSubtypeInterpreter<ItemStack> { stack, _ ->
                val entityType: EntityType<*>? = EntityDnaItem.getEntityType(stack)
                entityType?.toShortString() ?: "no_entity"
            })

        registration.registerSubtypeInterpreter(
            ModItems.GMO_CELL.get(),
            IIngredientSubtypeInterpreter<ItemStack> { stack, _ ->
                val entityType: EntityType<*>? = EntityDnaItem.getEntityType(stack)
                entityType?.toShortString() ?: "no_entity"
            })

        registration.registerSubtypeInterpreter(
            ModItems.PLASMID.get(),
            IIngredientSubtypeInterpreter<ItemStack> { stack, _ ->
                val gene: Gene? = stack.getGene()
                gene?.id?.toString() ?: "no_gene"
            })

        registration.registerSubtypeInterpreter(
            ModItems.DNA_HELIX.get(),
            IIngredientSubtypeInterpreter<ItemStack> { stack, _ ->
                val gene: Gene? = stack.getGene()
                gene?.id?.toString() ?: "no_gene"
            })

        registration.registerSubtypeInterpreter(
            ModItems.GMO_DNA_HELIX.get(),
            IIngredientSubtypeInterpreter<ItemStack> { stack, _ ->
                val gene: Gene? = stack.getGene()
                gene?.id?.toString() ?: "no_gene"
            })

    }

}