package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.Items
import net.minecraftforge.common.crafting.conditions.IConditionBuilder
import java.util.function.Consumer

class ModRecipeProvider(
    pGenerator: DataGenerator
) : RecipeProvider(pGenerator), IConditionBuilder {

    override fun buildCraftingRecipes(pFinishedRecipeConsumer: Consumer<FinishedRecipe>) {
//        advancedIncubatorRecipe.save(pFinishedRecipeConsumer)
        airborneDispersalDeviceRecipe.save(pFinishedRecipeConsumer)
        antiFieldBlockRecipe.save(pFinishedRecipeConsumer)
        antiFieldOrbRecipe.save(pFinishedRecipeConsumer)
//        antiPlasmidRecipe.save(pFinishedRecipeConsumer)
        bloodPurifierRecipe.save(pFinishedRecipeConsumer)
        cellAnalyzerRecipe.save(pFinishedRecipeConsumer)
        cloningMachineRecipe.save(pFinishedRecipeConsumer)
        coalGeneratorRecipe.save(pFinishedRecipeConsumer)
        dnaDecryptorRecipe.save(pFinishedRecipeConsumer)
        dnaExtractorRecipe.save(pFinishedRecipeConsumer)
        dragonHealthCrystalRecipe.save(pFinishedRecipeConsumer)
        incubatorRecipe.save(pFinishedRecipeConsumer)
        overclockerRecipe.save(pFinishedRecipeConsumer)
        plasmidRecipe.save(pFinishedRecipeConsumer)
        plasmidInfuserRecipe.save(pFinishedRecipeConsumer)
        plasmidInjectorRecipe.save(pFinishedRecipeConsumer)
        scraperRecipe.save(pFinishedRecipeConsumer)
        syringeRecipe.save(pFinishedRecipeConsumer)
    }

    //FIXME: Uses incubator instead of advanced incubator (NYI)
    private val advancedIncubatorRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.INCUBATOR.get(), 1)
            .define('O', Items.OBSIDIAN)
            .define('I', ModBlocks.INCUBATOR.get())
            .define('E', Items.END_STONE)
            .pattern("OOO")
            .pattern("OIO")
            .pattern("EEE")

    private val airborneDispersalDeviceRecipe =
        NoAdvancementShapedRecipeBuilder(ModBlocks.AIRBORNE_DISPERSAL_DEVICE.get(), 1)
            .define('D', Items.DIAMOND)
            .define('P', Items.DISPENSER)
            .define('C', Items.CLOCK)
            .define('R', Items.REDSTONE)
            .pattern("DPD")
            .pattern("DCD")
            .pattern("DRD")

    private val antiFieldBlockRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.ANTI_FIELD_BLOCK.get(), 1)
            .define('0', ModItems.ANTI_FIELD_ORB.get())
            .define('L', Items.REDSTONE_LAMP)
            .pattern("   ")
            .pattern(" 0 ")
            .pattern(" L ")

    private val antiFieldOrbRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModItems.ANTI_FIELD_ORB.get(), 1)
            .define('E', Items.ENDER_PEARL)
            .define('G', Items.GLASS)
            .define('F', Items.FERMENTED_SPIDER_EYE)
            .pattern("EGE")
            .pattern("GFG")
            .pattern("EGE")

    //FIXME: Uses Plasmid instead of Anti-Plasmid (NYI)
    private val antiPlasmidRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModItems.PLASMID.get(), 1)
            .define('D', ModItems.DNA_HELIX.get())
            .define('F', Items.FERMENTED_SPIDER_EYE)
            .pattern("DDD")
            .pattern("DFD")
            .pattern("DDD")

    private val bloodPurifierRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.BLOOD_PURIFIER.get(), 1)
            .define('I', Items.IRON_INGOT)
            .define('W', Items.WHITE_WOOL)
            .define('S', ModItems.SYRINGE.get())
            .define('B', Items.BUCKET)
            .pattern("IWI")
            .pattern("SBS")
            .pattern("IWI")

    private val cellAnalyzerRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.CELL_ANALYZER.get(), 1)
            .define('I', Items.IRON_INGOT)
            .define('S', ModItems.SYRINGE.get())
            .define('R', Items.REDSTONE)
            .pattern("III")
            .pattern("ISI")
            .pattern("IRI")

    private val cloningMachineRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.CLONING_MACHINE.get(), 1)
            .define('G', Items.GOLD_BLOCK)
            .define('D', Items.DIAMOND)
            .define('N', Items.NETHER_STAR)
            .pattern("GGG")
            .pattern("DND")
            .pattern("GGG")

    private val coalGeneratorRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.COAL_GENERATOR.get(), 1)
            .define('I', Items.IRON_INGOT)
            .define('G', Items.GLASS)
            .define('F', Items.FURNACE)
            .define('R', Items.REDSTONE)
            .pattern("III")
            .pattern("GFG")
            .pattern("GRG")

    private val dnaDecryptorRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.DNA_DECRYPTOR.get(), 1)
            .define('I', Items.IRON_INGOT)
            .define('L', Items.GLASS)
            .define('D', ModItems.DNA_HELIX.get())
            .define('G', Items.GOLD_INGOT)
            .pattern("ILI")
            .pattern("DGD")
            .pattern("ILI")

    private val dnaExtractorRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.DNA_EXTRACTOR.get(), 1)
            .define('I', Items.IRON_INGOT)
            .define('P', Items.STICKY_PISTON)
            .define('C', ModItems.CELL.get())
            .pattern("III")
            .pattern("PCP")
            .pattern("III")

    private val dragonHealthCrystalRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModItems.DRAGON_HEALTH_CRYSTAL.get(), 1)
            .define('D', Items.DIAMOND)
            .define('E', Items.END_CRYSTAL)
            .pattern("DED")
            .pattern("EDE")
            .pattern("DED")

    private val incubatorRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.INCUBATOR.get(), 1)
            .define('I', Items.IRON_INGOT)
            .define('G', Items.GLASS)
            .define('B', Items.BLAZE_ROD)
            .define('S', Items.STONE)
            .define('R', Items.REDSTONE)
            .pattern("IGI")
            .pattern("IBI")
            .pattern("SRS")

    private val overclockerRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModItems.OVERCLOCKER.get(), 1)
            .define('C', ModItems.CELL.get())
            .define('L', Items.LAPIS_LAZULI)
            .define('O', Items.CLOCK)
            .pattern("CLC")
            .pattern("LOL")
            .pattern("CLC")

    private val plasmidRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModItems.PLASMID.get(), 1)
            .define('D', ModItems.DNA_HELIX.get())
            .pattern("DDD")
            .pattern("D D")
            .pattern("DDD")

    private val plasmidInfuserRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.PLASMID_INFUSER.get(), 1)
            .define('I', Items.IRON_INGOT)
            .define('L', ModItems.PLASMID.get())
            .define('P', Items.PISTON)
            .define('D', Items.DIAMOND)
            .pattern("III")
            .pattern("LPL")
            .pattern("IDI")

    private val plasmidInjectorRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModBlocks.PLASMID_INJECTOR.get(), 1)
            .define('I', Items.IRON_INGOT)
            .define('S', ModItems.SYRINGE.get())
            .define('B', Items.BUCKET)
            .pattern("ISI")
            .pattern("SBS")
            .pattern("ISI")

    private val scraperRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModItems.SCRAPER.get(), 1)
            .define('I', Items.IRON_INGOT)
            .define('S', Items.STICK)
            .pattern(" IS")
            .pattern(" SI")
            .pattern("S  ")

    private val syringeRecipe: NoAdvancementShapedRecipeBuilder =
        NoAdvancementShapedRecipeBuilder(ModItems.SYRINGE.get(), 1)
            .define('P', Items.PISTON)
            .define('G', Items.GLASS)
            .define('B', Items.GLASS_BOTTLE)
            .define('A', Items.ARROW)
            .pattern(" P ")
            .pattern("GBG")
            .pattern("GAG")

}