package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types.DupeCellRecipeBuilder
import dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types.GmoRecipeBuilder
import dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types.SingletonRecipeBuilder
import dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types.VirusRecipeBuilder
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.high_temp.BlackDeathRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.high_temp.SetPotionEntityRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, lookupProvider) {

    override fun buildRecipes(pRecipeOutput: RecipeOutput) {

        for (shapedRecipe in shapedRecipes) {
            shapedRecipe.save(pRecipeOutput)
        }

        setAntiPlasmid.save(pRecipeOutput, OtherUtil.modResource("set_anti_plasmid"))
        unsetAntiPlasmid.save(pRecipeOutput, OtherUtil.modResource("unset_anti_plasmid"))

        for (gmoRecipe in gmoRecipes) {
            gmoRecipe.save(pRecipeOutput)
        }

        for (mutationRecipe in mutationRecipes) {
            mutationRecipe.save(pRecipeOutput)
        }

        setPotionEntity.save(pRecipeOutput)
        blackDeath.save(pRecipeOutput)

        dupeCell.save(pRecipeOutput)
        dupeGmoCell.save(pRecipeOutput)

        for (virusRecipe in virusRecipes) {
            virusRecipe.save(pRecipeOutput)
        }

    }

    companion object {

        private val setAntiPlasmid = SpecialRecipeBuilder.special(::SetAntiPlasmidRecipe)
        private val unsetAntiPlasmid = SpecialRecipeBuilder.special(::UnsetAntiPlasmidRecipe)

        private val shapedRecipes = listOf(
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ADVANCED_INCUBATOR.get())
                .pattern("OOO")
                .pattern("OIO")
                .pattern("EEE")
                .define('O', Tags.Items.OBSIDIANS)
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
                .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
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
                .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('F', Items.FURNACE)
                .define('R', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_cell_analyzer", has(ModBlocks.CELL_ANALYZER.get())),

            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DNA_DECRYPTOR.get())
                .pattern("ILI")
                .pattern("DGD")
                .pattern("ILI")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('L', Tags.Items.GLASS_BLOCKS_COLORLESS)
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
                .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('B', Items.BLAZE_ROD)
                .define('S', Tags.Items.STONES)
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
                .define('G', Tags.Items.GLASS_BLOCKS_COLORLESS)
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
                .define('O', Tags.Items.OBSIDIANS)
                .unlockedBy("has_syringe", has(ModItems.SYRINGE.get())),

            modonomicon()
        )

        private fun modonomicon(): ShapedRecipeBuilder {
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

        //TODO: Make Patchouli datagen eventually too

        private val gmoRecipes = listOf(
            GmoRecipeBuilder(EntityType.BLAZE, Items.GLOWSTONE_DUST, ModGenes.BIOLUMINESCENCE, 0.85f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, ModGenes.BIOLUMINESCENCE, 0.85f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.VILLAGER, Items.EMERALD, ModGenes.EMERALD_HEART, 0.85f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.SHULKER, Items.EMERALD_BLOCK, ModGenes.KEEP_INVENTORY, 0.45f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.RABBIT, Items.GOLDEN_BOOTS, ModGenes.SPEED, 0.65f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.RABBIT, Items.EMERALD, ModGenes.LUCK, 0.75f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.IRON_GOLEM, Items.GOLDEN_APPLE, ModGenes.REGENERATION, 0.3f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.CHICKEN, Items.EGG, ModGenes.LAY_EGG, 1f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.PIG, Items.PORKCHOP, ModGenes.MEATY, 1f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.ENDERMAN, Items.ENDER_PEARL, ModGenes.TELEPORT, 0.45f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.MORE_HEARTS, 0.2f)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),
            GmoRecipeBuilder(EntityType.MOOSHROOM, Items.MUSHROOM_STEM, ModGenes.PHOTOSYNTHESIS, 0.7f)
                .unlockedBy("has_cell", has(ModItems.CELL.get()))
        )

        val mutationRecipes = listOf(
            GmoRecipeBuilder(EntityType.ENDER_DRAGON, Items.ELYTRA, ModGenes.FLIGHT, 0.55f, isMutation = true)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(
                EntityType.POLAR_BEAR,
                Items.NETHERITE_SWORD,
                ModGenes.STRENGTH_TWO,
                0.5f,
                isMutation = true
            ).unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(
                EntityType.SHULKER,
                Items.NETHERITE_CHESTPLATE,
                ModGenes.RESISTANCE_TWO,
                0.5f,
                isMutation = true
            ).unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(EntityType.POLAR_BEAR, Items.DIAMOND_SWORD, ModGenes.CLAWS_TWO, 0.75f, isMutation = true)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(EntityType.RABBIT, Items.DIAMOND_BOOTS, ModGenes.SPEED_TWO, 0.5f, isMutation = true)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(EntityType.OCELOT, Items.NETHERITE_BOOTS, ModGenes.SPEED_FOUR, 0.5f, isMutation = true)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(EntityType.RABBIT, Items.NETHERITE_PICKAXE, ModGenes.HASTE_TWO, 0.35f, isMutation = true)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(
                EntityType.SILVERFISH,
                Items.NETHERITE_PICKAXE,
                ModGenes.EFFICIENCY_FOUR,
                0.25f,
                isMutation = true
            ).unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(
                EntityType.ZOMBIE,
                Items.FERMENTED_SPIDER_EYE,
                ModGenes.SCARE_ZOMBIES,
                0.5f,
                isMutation = true
            ).unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(
                EntityType.SPIDER,
                Items.FERMENTED_SPIDER_EYE,
                ModGenes.SCARE_SPIDERS,
                0.5f,
                isMutation = true
            ).unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(
                EntityType.ENDER_DRAGON,
                Items.ENCHANTED_GOLDEN_APPLE,
                ModGenes.REGENERATION_FOUR,
                0.35f,
                isMutation = true
            ).unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(EntityType.PIG, Items.BLAZE_POWDER, ModGenes.MEATY_TWO, 0.75f, isMutation = true)
                .unlockedBy("has_cell", has(ModItems.CELL.get())),

            GmoRecipeBuilder(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.MORE_HEARTS_TWO, 0.25f, true)
                .unlockedBy("has_cell", has(ModItems.CELL.get()))
        )

        private val setPotionEntity =
            SingletonRecipeBuilder(SetPotionEntityRecipe.INSTANCE, Items.POTION, "incubator/set_potion_entity")
                .unlockedBy("has_cell", has(ModItems.CELL.get()))

        private val blackDeath =
            SingletonRecipeBuilder(BlackDeathRecipe.INSTANCE, ModItems.DNA_HELIX.get(), "incubator/black_death")
                .unlockedBy("has_cell", has(ModItems.CELL.get()))

        private val dupeCell = DupeCellRecipeBuilder()
            .unlockedBy("has_cell", has(ModItems.CELL.get()))

        private val dupeGmoCell = DupeCellRecipeBuilder(true)
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

    }

}