package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.world.item.Items
import net.minecraftforge.common.Tags
import net.minecraftforge.common.crafting.conditions.IConditionBuilder
import java.util.function.Consumer

class ModRecipeProvider(pOutput: PackOutput) : RecipeProvider(pOutput), IConditionBuilder {

	override fun buildRecipes(pWriter: Consumer<FinishedRecipe>) {

		shapedRecipes(pWriter)
		specialRecipes(pWriter)

	}

	private fun specialRecipes(pWriter: Consumer<FinishedRecipe>) {
		val setAntiPlasmid = SpecialRecipeBuilder.special(ModRecipeSerializers.SET_ANTI_PLASMID.get())
		val unsetAntiPlasmid = SpecialRecipeBuilder.special(ModRecipeSerializers.UNSET_ANTI_PLASMID.get())

		setAntiPlasmid.save(pWriter, GeneticsResequenced.modResource("set_anti_plasmid").toString())
		unsetAntiPlasmid.save(pWriter, GeneticsResequenced.modResource("unset_anti_plasmid").toString())
	}

	private fun shapedRecipes(pWriter: Consumer<FinishedRecipe>) {
		val shapedRecipes = listOf(
			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ADVANCED_INCUBATOR.get())
				.pattern("OOO")
				.pattern("OIO")
				.pattern("EEE")
				.define('O', Tags.Items.OBSIDIAN)
				.define('I', ModBlocks.INCUBATOR.get())
				.define('E', Tags.Items.END_STONES)
				.unlockedBy("has_incubator", has(ModBlocks.INCUBATOR.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ANTI_FIELD_BLOCK.get())
				.pattern("   ")
				.pattern(" O ")
				.pattern(" L ")
				.define('O', ModItems.ANTI_FIELD_ORB.get())
				.define('L', Items.REDSTONE_LAMP)
				.unlockedBy("has_anti_field_orb", has(ModItems.ANTI_FIELD_ORB.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ANTI_FIELD_ORB.get())
				.pattern("EGE")
				.pattern("GFG")
				.pattern("EGE")
				.define('E', Tags.Items.ENDER_PEARLS)
				.define('G', Tags.Items.GLASS_COLORLESS)
				.define('F', Items.FERMENTED_SPIDER_EYE)
				.unlockedBy("has_plasmid", has(ModItems.PLASMID.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ANTI_PLASMID.get())
				.pattern("DDD")
				.pattern("DFD")
				.pattern("DDD")
				.define('D', ModItems.DNA_HELIX.get())
				.define('F', Items.FERMENTED_SPIDER_EYE)
				.unlockedBy("has_plasmid", has(ModItems.PLASMID.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLOOD_PURIFIER.get())
				.pattern("IWI")
				.pattern("SBS")
				.pattern("IWI")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('W', Items.WHITE_WOOL)
				.define('S', ModItems.SYRINGE.get())
				.define('B', Items.BUCKET)
				.unlockedBy("has_syringe", has(ModItems.SYRINGE.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CELL_ANALYZER.get())
				.pattern("III")
				.pattern("ISI")
				.pattern("IRI")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('S', ModItems.SYRINGE.get())
				.define('R', Tags.Items.DUSTS_REDSTONE)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COAL_GENERATOR.get())
				.pattern("III")
				.pattern("GFG")
				.pattern("GRG")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('G', Tags.Items.GLASS_COLORLESS)
				.define('F', Items.FURNACE)
				.define('R', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_cell_analyzer", has(ModBlocks.CELL_ANALYZER.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DNA_DECRYPTOR.get())
				.pattern("ILI")
				.pattern("DGD")
				.pattern("ILI")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('L', Tags.Items.GLASS_COLORLESS)
				.define('D', ModItems.DNA_HELIX.get())
				.define('G', Tags.Items.INGOTS_GOLD)
				.unlockedBy("has_dna_extractor", has(ModBlocks.DNA_EXTRACTOR.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DNA_EXTRACTOR.get())
				.pattern("III")
				.pattern("PCP")
				.pattern("III")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('P', Items.STICKY_PISTON)
				.define('C', ModItems.CELL.get())
				.unlockedBy("has_cell_analyzer", has(ModBlocks.CELL_ANALYZER.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DRAGON_HEALTH_CRYSTAL.get())
				.pattern("DED")
				.pattern("EDE")
				.pattern("DED")
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('E', Items.END_CRYSTAL)
				.unlockedBy("has_plasmid", has(ModItems.PLASMID.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.INCUBATOR.get())
				.pattern("IGI")
				.pattern("IBI")
				.pattern("SRS")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('G', Tags.Items.GLASS_COLORLESS)
				.define('B', Items.BLAZE_ROD)
				.define('S', Tags.Items.STONE)
				.define('R', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_brewing_stand", has(Items.BREWING_STAND)),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.OVERCLOCKER.get())
				.pattern("CLC")
				.pattern("LOL")
				.pattern("CLC")
				.define('C', ModItems.CELL.get())
				.define('L', Tags.Items.GEMS_LAPIS)
				.define('O', Items.CLOCK)
				.unlockedBy("has_cell_analyzer", has(ModBlocks.CELL_ANALYZER.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PLASMID.get())
				.pattern("DDD")
				.pattern("D D")
				.pattern("DDD")
				.define('D', ModItems.DNA_HELIX.get())
				.unlockedBy("has_plasmid_injector", has(ModBlocks.PLASMID_INJECTOR.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PLASMID_INFUSER.get())
				.pattern("III")
				.pattern("LPL")
				.pattern("IDI")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('L', ModItems.PLASMID.get())
				.define('P', Items.PISTON)
				.define('D', Tags.Items.GEMS_DIAMOND)
				.unlockedBy("has_dna_decryptor", has(ModBlocks.DNA_DECRYPTOR.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PLASMID_INJECTOR.get())
				.pattern("ISI")
				.pattern("SBS")
				.pattern("ISI")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('S', ModItems.SYRINGE.get())
				.define('B', Items.BUCKET)
				.unlockedBy("has_plasmid_infuser", has(ModBlocks.PLASMID_INFUSER.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SCRAPER.get())
				.pattern(" IS")
				.pattern(" SI")
				.pattern("S  ")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('S', Items.STICK)
				.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON)),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SYRINGE.get())
				.pattern(" P ")
				.pattern("GBG")
				.pattern("GAG")
				.define('P', Items.PISTON)
				.define('G', Tags.Items.GLASS_COLORLESS)
				.define('B', Items.GLASS_BOTTLE)
				.define('A', Items.ARROW)
				.unlockedBy("has_scraper", has(ModItems.SCRAPER.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METAL_SYRINGE.get())
				.pattern("IOI")
				.pattern("ISI")
				.pattern("IDI")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('D', Tags.Items.GEMS_DIAMOND)
				.define('S', ModItems.SYRINGE.get())
				.define('O', Tags.Items.OBSIDIAN)
				.unlockedBy("has_syringe", has(ModItems.SYRINGE.get())),

			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GENE_CHECKER.get())
				.pattern("III")
				.pattern("IGI")
				.pattern("ISI")
				.define('I', Tags.Items.INGOTS_IRON)
				.define('G', Tags.Items.GLASS)
				.define('S', ModItemTagsProvider.SYRINGES)
				.unlockedBy("has_scraper", has(ModItems.SCRAPER.get())),
		)

		for (recipe in shapedRecipes) {
			recipe.save(pWriter)
		}
	}

}