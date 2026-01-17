package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.aaron.AaronExtensions.asIngredient
import dev.aaronhowser.mods.aaron.AaronExtensions.partialNbtIngredient
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder.BasicIncubatorRecipeBuilder
import dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder.DupeCellRecipeBuilder
import dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder.GmoRecipeBuilder
import dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder.SingletonRecipeBuilder
import dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder.VirusRecipeBuilder
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BlackDeathRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.SetPotionEntityRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.*
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraftforge.common.Tags
import net.minecraftforge.common.crafting.conditions.IConditionBuilder
import java.util.function.Consumer

class ModRecipeProvider(pOutput: PackOutput) : RecipeProvider(pOutput), IConditionBuilder {

	override fun buildRecipes(pWriter: Consumer<FinishedRecipe>) {

		shapedRecipes(pWriter)
		specialRecipes(pWriter)
		incubator(pWriter)

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

	private fun incubator(pWriter: Consumer<FinishedRecipe>) {

		val basicIncubatorRecipes = listOf(
			BasicIncubatorRecipeBuilder(
				DnaHelixItem.getHelixStack(ModGenes.BASIC).partialNbtIngredient(),
				OtherUtil.getPotionStack(ModPotions.SUBSTRATE.get()).partialNbtIngredient(),
				OtherUtil.getPotionStack(ModPotions.CELL_GROWTH.get()),
				"cell_growth"
			),

			BasicIncubatorRecipeBuilder(
				DataComponentIngredient.of(
					false,
					DnaHelixItem.getHelixStack(ModGenes.REGENERATION, lookupProvider.get())
				),
				DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)),
				OtherUtil.getPotionStack(ModPotions.PANACEA),
				"panacea"
			),

			BasicIncubatorRecipeBuilder(
				DataComponentIngredient.of(
					false,
					DnaHelixItem.getHelixStack(ModGenes.EMERALD_HEART, lookupProvider.get())
				),
				DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)),
				OtherUtil.getPotionStack(ModPotions.ZOMBIFY_VILLAGER),
				"zombify_villager"
			)
		)

		val gmoRecipes = listOf(
			GmoRecipeBuilder(
				EntityType.BLAZE,
				Items.GLOWSTONE_DUST.asIngredient(),
				ModGenes.BIOLUMINESCENCE,
				0.85f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.MAGMA_CUBE,
				Items.GLOWSTONE_DUST.asIngredient(),
				ModGenes.BIOLUMINESCENCE,
				0.85f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.VILLAGER,
				Items.EMERALD.asIngredient(),
				ModGenes.EMERALD_HEART,
				0.85f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.SHULKER,
				Items.EMERALD_BLOCK.asIngredient(),
				ModGenes.KEEP_INVENTORY,
				0.45f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.RABBIT,
				Items.GOLDEN_BOOTS.asIngredient(),
				ModGenes.SPEED,
				0.65f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.RABBIT,
				Items.EMERALD.asIngredient(),
				ModGenes.LUCK,
				0.75f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.IRON_GOLEM,
				Items.GOLDEN_APPLE.asIngredient(),
				ModGenes.REGENERATION,
				0.3f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.CHICKEN,
				Items.EGG.asIngredient(),
				ModGenes.LAY_EGG,
				1f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.PIG,
				Items.PORKCHOP.asIngredient(),
				ModGenes.MEATY,
				1f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.ENDERMAN,
				Items.ENDER_PEARL.asIngredient(),
				ModGenes.TELEPORT,
				0.45f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.ENDERMAN,
				Items.GOLDEN_APPLE.asIngredient(),
				ModGenes.MORE_HEARTS,
				0.2f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.MOOSHROOM,
				Items.MUSHROOM_STEM.asIngredient(),
				ModGenes.PHOTOSYNTHESIS,
				0.7f
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get()))
		)

		val mutationRecipes = listOf(
			GmoRecipeBuilder(
				EntityType.ENDER_DRAGON,
				Items.ELYTRA.asIngredient(),
				ModGenes.FLIGHT,
				0.55f,
				needsMutationPotion = true
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.POLAR_BEAR,
				Items.NETHERITE_SWORD.asIngredient(),
				ModGenes.STRENGTH_TWO,
				0.5f,
				needsMutationPotion = true
			).unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.SHULKER,
				Items.NETHERITE_CHESTPLATE.asIngredient(),
				ModGenes.RESISTANCE_TWO,
				0.5f,
				needsMutationPotion = true
			).unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.POLAR_BEAR,
				Items.DIAMOND_SWORD.asIngredient(),
				ModGenes.CLAWS_TWO,
				0.75f,
				needsMutationPotion = true
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.RABBIT,
				Items.DIAMOND_BOOTS.asIngredient(),
				ModGenes.SPEED_TWO,
				0.5f,
				needsMutationPotion = true
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.OCELOT,
				Items.NETHERITE_BOOTS.asIngredient(),
				ModGenes.SPEED_FOUR,
				0.5f,
				needsMutationPotion = true
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.RABBIT,
				Items.NETHERITE_PICKAXE.asIngredient(),
				ModGenes.HASTE_TWO,
				0.35f,
				needsMutationPotion = true
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.SILVERFISH,
				Items.NETHERITE_PICKAXE.asIngredient(),
				ModGenes.EFFICIENCY_FOUR,
				0.25f,
				needsMutationPotion = true
			).unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.ZOMBIE,
				Items.FERMENTED_SPIDER_EYE.asIngredient(),
				ModGenes.SCARE_ZOMBIES,
				0.5f,
				needsMutationPotion = true
			).unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.SPIDER,
				Items.FERMENTED_SPIDER_EYE.asIngredient(),
				ModGenes.SCARE_SPIDERS,
				0.5f,
				needsMutationPotion = true
			).unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.ENDER_DRAGON,
				Items.ENCHANTED_GOLDEN_APPLE.asIngredient(),
				ModGenes.REGENERATION_FOUR,
				0.35f,
				needsMutationPotion = true
			).unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.PIG,
				Items.BLAZE_POWDER.asIngredient(),
				ModGenes.MEATY_TWO,
				0.75f,
				needsMutationPotion = true
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get())),

			GmoRecipeBuilder(
				EntityType.ENDERMAN,
				Items.GOLDEN_APPLE.asIngredient(),
				ModGenes.MORE_HEARTS_TWO,
				0.25f,
				needsMutationPotion = true
			)
				.unlockedBy("has_cell", has(ModItems.CELL.get()))
		)

//		val setPotionEntity =
//			SingletonRecipeBuilder(
//				SetPotionEntityRecipe,
//				Items.POTION,
//				"incubator/set_potion_entity"
//			).unlockedBy("has_cell", has(ModItems.CELL.get()))

		val blackDeath =
			SingletonRecipeBuilder(
				BlackDeathRecipe,
				ModItems.DNA_HELIX.get(),
			).unlockedBy("has_cell", has(ModItems.CELL.get()))

		val dupeCell = DupeCellRecipeBuilder(ModItems.CELL.get(), 8, "dupe_cell")
			.unlockedBy("has_cell", has(ModItems.CELL.get()))

		val dupeGmoCell = DupeCellRecipeBuilder(ModItems.GMO_CELL.get(), 4, "dupe_gmo_cell")
			.unlockedBy("has_gmo_cell", has(ModItems.GMO_CELL.get()))

		val virusRecipes = listOf(
			VirusRecipeBuilder(ModGenes.POISON_IMMUNITY, ModGenes.POISON),
			VirusRecipeBuilder(ModGenes.WITHER_HIT, ModGenes.POISON_FOUR),
			VirusRecipeBuilder(ModGenes.WITHER_PROOF, ModGenes.WITHER),
			VirusRecipeBuilder(ModGenes.STRENGTH, ModGenes.WEAKNESS),
			VirusRecipeBuilder(ModGenes.NIGHT_VISION, ModGenes.BLINDNESS),
			VirusRecipeBuilder(ModGenes.SPEED, ModGenes.SLOWNESS),
			VirusRecipeBuilder(ModGenes.SPEED_TWO, ModGenes.SLOWNESS_FOUR),
			VirusRecipeBuilder(ModGenes.SPEED_FOUR, ModGenes.SLOWNESS_SIX),
			VirusRecipeBuilder(ModGenes.MILKY, ModGenes.NAUSEA),
			VirusRecipeBuilder(ModGenes.MEATY, ModGenes.NAUSEA),
			VirusRecipeBuilder(ModGenes.LAY_EGG, ModGenes.NAUSEA),
			VirusRecipeBuilder(ModGenes.NO_HUNGER, ModGenes.HUNGER),
			VirusRecipeBuilder(ModGenes.FIRE_PROOF, ModGenes.FLAMBE),
			VirusRecipeBuilder(ModGenes.LUCK, ModGenes.CURSED),
			VirusRecipeBuilder(ModGenes.HASTE, ModGenes.MINING_FATIGUE),
			VirusRecipeBuilder(ModGenes.SCARE_CREEPERS, ModGenes.GREEN_DEATH),
			VirusRecipeBuilder(ModGenes.SCARE_SKELETONS, ModGenes.UN_UNDEATH),
			VirusRecipeBuilder(ModGenes.SCARE_ZOMBIES, ModGenes.UN_UNDEATH),
			VirusRecipeBuilder(ModGenes.RESISTANCE, ModGenes.GRAY_DEATH),
			VirusRecipeBuilder(ModGenes.DRAGON_BREATH, ModGenes.WHITE_DEATH)
		)

		for (recipe in basicIncubatorRecipes) {
			recipe.save(pWriter, recipe.name)
		}

		for (recipe in gmoRecipes) {
			recipe.save(pWriter)
		}

		for (recipe in mutationRecipes) {
			recipe.save(pWriter)
		}

//		setPotionEntity.save(pWriter)
		blackDeath.save(pWriter, "incubator/black_death")
		dupeCell.save(pWriter)
		dupeGmoCell.save(pWriter)

		for (recipe in virusRecipes) {
			recipe.save(pWriter)
		}
	}

}