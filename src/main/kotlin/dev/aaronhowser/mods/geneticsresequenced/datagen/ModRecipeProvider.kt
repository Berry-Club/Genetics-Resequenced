package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, lookupProvider) {

    override fun buildRecipes(pRecipeOutput: RecipeOutput) {
        advancedIncubator.save(pRecipeOutput)
        antiFieldBlock.save(pRecipeOutput)
        antiFieldOrb.save(pRecipeOutput)
        antiPlasmid.save(pRecipeOutput)
        setAntiPlasmid.save(pRecipeOutput, OtherUtil.modResource("set_anti_plasmid"))
        unsetAntiPlasmid.save(pRecipeOutput, OtherUtil.modResource("unset_anti_plasmid"))
        bloodPurifier.save(pRecipeOutput)
        cellAnalyzer.save(pRecipeOutput)
        coalGenerator.save(pRecipeOutput)
        dnaDecryptor.save(pRecipeOutput)
        dnaExtractor.save(pRecipeOutput)
        dragonHealthCrystal.save(pRecipeOutput)
        incubator.save(pRecipeOutput)
        overclocker.save(pRecipeOutput)
        plasmid.save(pRecipeOutput)
        plasmidInfuser.save(pRecipeOutput)
        plasmidInjector.save(pRecipeOutput)
        scraper.save(pRecipeOutput)
        syringe.save(pRecipeOutput)
        modonomicon().save(pRecipeOutput)
    }

    companion object {
        private val advancedIncubator: ShapedRecipeBuilder =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ADVANCED_INCUBATOR.get())
                .pattern("OOO")
                .pattern("OIO")
                .pattern("EEE")
                .define('O', Items.OBSIDIAN)
                .define('I', ModBlocks.INCUBATOR.get())
                .define('E', Items.END_STONE)
                .unlockedBy("has_incubator", has(ModBlocks.INCUBATOR.get()))

        private val antiFieldBlock = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ANTI_FIELD_BLOCK.get())
            .pattern("   ")
            .pattern(" O ")
            .pattern(" L ")
            .define('O', ModItems.ANTI_FIELD_ORB.get())
            .define('L', Items.REDSTONE_LAMP)
            .unlockedBy("has_anti_field_orb", has(ModItems.ANTI_FIELD_ORB.get()))

        private val antiFieldOrb = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ANTI_FIELD_ORB.get())
            .pattern("EGE")
            .pattern("GFG")
            .pattern("EGE")
            .define('E', Items.ENDER_PEARL)
            .define('G', Items.GLASS)
            .define('F', Items.FERMENTED_SPIDER_EYE)
            .unlockedBy("has_plasmid", has(ModItems.PLASMID.get()))

        private val antiPlasmid = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ANTI_PLASMID.get())
            .pattern("DDD")
            .pattern("DFD")
            .pattern("DDD")
            .define('D', ModItems.DNA_HELIX.get())
            .define('F', Items.FERMENTED_SPIDER_EYE)
            .unlockedBy("has_plasmid", has(ModItems.PLASMID.get()))

        private val setAntiPlasmid = SpecialRecipeBuilder
            .special(::SetAntiPlasmidRecipe)

        private val unsetAntiPlasmid = SpecialRecipeBuilder
            .special(::UnsetAntiPlasmidRecipe)

        private val bloodPurifier = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLOOD_PURIFIER.get())
            .pattern("IWI")
            .pattern("SBS")
            .pattern("IWI")
            .define('I', Items.IRON_INGOT)
            .define('W', Items.WHITE_WOOL)
            .define('S', ModItems.SYRINGE.get())
            .define('B', Items.BUCKET)
            .unlockedBy("has_syringe", has(ModItems.SYRINGE.get()))

        private val cellAnalyzer = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CELL_ANALYZER.get())
            .pattern("III")
            .pattern("ISI")
            .pattern("IRI")
            .define('I', Items.IRON_INGOT)
            .define('S', ModItems.SYRINGE.get())
            .define('R', Items.REDSTONE)
            .unlockedBy("has_cell", has(ModItems.CELL.get()))

        private val coalGenerator = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COAL_GENERATOR.get())
            .pattern("III")
            .pattern("GFG")
            .pattern("GRG")
            .define('I', Items.IRON_INGOT)
            .define('G', Items.GLASS)
            .define('F', Items.FURNACE)
            .define('R', Items.REDSTONE)
            .unlockedBy("has_cell_analyzer", has(ModBlocks.CELL_ANALYZER.get()))

        private val dnaDecryptor = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DNA_DECRYPTOR.get())
            .pattern("ILI")
            .pattern("DGD")
            .pattern("ILI")
            .define('I', Items.IRON_INGOT)
            .define('L', Items.GLASS)
            .define('D', ModItems.DNA_HELIX.get())
            .define('G', Items.GOLD_INGOT)
            .unlockedBy("has_dna_extractor", has(ModBlocks.DNA_EXTRACTOR.get()))

        private val dnaExtractor = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DNA_EXTRACTOR.get())
            .pattern("III")
            .pattern("PCP")
            .pattern("III")
            .define('I', Items.IRON_INGOT)
            .define('P', Items.STICKY_PISTON)
            .define('C', ModItems.CELL.get())
            .unlockedBy("has_cell_analyzer", has(ModBlocks.CELL_ANALYZER.get()))

        private val dragonHealthCrystal =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRAGON_HEALTH_CRYSTAL.get())
                .pattern("DED")
                .pattern("EDE")
                .pattern("DED")
                .define('D', Items.DIAMOND)
                .define('E', Items.END_CRYSTAL)
                .unlockedBy("has_plasmid", has(ModItems.PLASMID.get()))

        private val incubator = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INCUBATOR.get())
            .pattern("IGI")
            .pattern("IBI")
            .pattern("SRS")
            .define('I', Items.IRON_INGOT)
            .define('G', Items.GLASS)
            .define('B', Items.BLAZE_ROD)
            .define('S', Items.STONE)
            .define('R', Items.REDSTONE)
            .unlockedBy("has_brewing_stand", has(Items.BREWING_STAND))

        private val overclocker = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.OVERCLOCKER.get())
            .pattern("CLC")
            .pattern("LOL")
            .pattern("CLC")
            .define('C', ModItems.CELL.get())
            .define('L', Items.LAPIS_LAZULI)
            .define('O', Items.CLOCK)
            .unlockedBy("has_cell_analyzer", has(ModBlocks.CELL_ANALYZER.get()))

        private val plasmid = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PLASMID.get())
            .pattern("DDD")
            .pattern("D D")
            .pattern("DDD")
            .define('D', ModItems.DNA_HELIX.get())
            .unlockedBy("has_plasmid_injector", has(ModBlocks.PLASMID_INJECTOR.get()))

        private val plasmidInfuser = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PLASMID_INFUSER.get())
            .pattern("III")
            .pattern("LPL")
            .pattern("IDI")
            .define('I', Items.IRON_INGOT)
            .define('L', ModItems.PLASMID.get())
            .define('P', Items.PISTON)
            .define('D', Items.DIAMOND)
            .unlockedBy("has_dna_decryptor", has(ModBlocks.DNA_DECRYPTOR.get()))

        private val plasmidInjector = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PLASMID_INJECTOR.get())
            .pattern("ISI")
            .pattern("SBS")
            .pattern("ISI")
            .define('I', Items.IRON_INGOT)
            .define('S', ModItems.SYRINGE.get())
            .define('B', Items.BUCKET)
            .unlockedBy("has_plasmid_infuser", has(ModBlocks.PLASMID_INFUSER.get()))

        private val scraper = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SCRAPER.get())
            .pattern(" IS")
            .pattern(" SI")
            .pattern("S  ")
            .define('I', Items.IRON_INGOT)
            .define('S', Items.STICK)
            .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))

        private val syringe = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SYRINGE.get())
            .pattern(" P ")
            .pattern("GBG")
            .pattern("GAG")
            .define('P', Items.PISTON)
            .define('G', Items.GLASS)
            .define('B', Items.GLASS_BOTTLE)
            .define('A', Items.ARROW)
            .unlockedBy("has_scraper", has(ModItems.SCRAPER.get()))

        fun modonomicon(): ShapedRecipeBuilder {
            val bookStack = com.klikli_dev.modonomicon.registry.ItemRegistry.MODONOMICON.get().itemStack
            val bookIdComponent = com.klikli_dev.modonomicon.registry.DataComponentRegistry.BOOK_ID.get()

            bookStack.set(bookIdComponent, OtherUtil.modResource("guide"))

            val recipe = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, bookStack)
                .pattern("OB")
                .define('O', ModItems.ORGANIC_MATTER)
                .define('B', Items.BOOK)
                .unlockedBy("has_scraper", has(ModItems.SCRAPER.get()))

            return recipe
        }
    }

}