package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types.*
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.BaseModGenes
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BlackDeathRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.SetPotionEntityRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
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

        for (basicIncubatorRecipe in basicIncubatorRecipe) {
            basicIncubatorRecipe.save(pRecipeOutput)
        }

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

        modonomicon(),

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GENE_CHECKER.get())
            .pattern("III")
            .pattern("IGI")
            .pattern("ISI")
            .define('I', Tags.Items.INGOTS_IRON)
            .define('G', Tags.Items.GLASS_BLOCKS)
            .define('S', ModItemTagsProvider.SYRINGES)
            .unlockedBy("has_scraper", has(ModItems.SCRAPER.get())),
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

    private val basicIncubatorRecipe = listOf(
        BasicIncubatorRecipeBuilder(
            DataComponentIngredient.of(
                false,
                DnaHelixItem.getHelixStack(BaseModGenes.BASIC, lookupProvider.get())
            ),
            DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.SUBSTRATE)),
            OtherUtil.getPotionStack(ModPotions.CELL_GROWTH),
            "cell_growth"
        ),

        BasicIncubatorRecipeBuilder(
            DataComponentIngredient.of(
                false,
                DnaHelixItem.getHelixStack(BaseModGenes.REGENERATION, lookupProvider.get())
            ),
            DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)),
            OtherUtil.getPotionStack(ModPotions.PANACEA),
            "panacea"
        ),

        BasicIncubatorRecipeBuilder(
            DataComponentIngredient.of(
                false,
                DnaHelixItem.getHelixStack(BaseModGenes.EMERALD_HEART, lookupProvider.get())
            ),
            DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)),
            OtherUtil.getPotionStack(ModPotions.ZOMBIFY_VILLAGER),
            "zombify_villager"
        )

    )

    private val gmoRecipes = listOf(
        GmoRecipeBuilder(EntityType.BLAZE, Items.GLOWSTONE_DUST, BaseModGenes.BIOLUMINESCENCE, 0.85f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, BaseModGenes.BIOLUMINESCENCE, 0.85f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.VILLAGER, Items.EMERALD, BaseModGenes.EMERALD_HEART, 0.85f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.SHULKER, Items.EMERALD_BLOCK, BaseModGenes.KEEP_INVENTORY, 0.45f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.RABBIT, Items.GOLDEN_BOOTS, BaseModGenes.SPEED, 0.65f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.RABBIT, Items.EMERALD, BaseModGenes.LUCK, 0.75f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.IRON_GOLEM, Items.GOLDEN_APPLE, BaseModGenes.REGENERATION, 0.3f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.CHICKEN, Items.EGG, BaseModGenes.LAY_EGG, 1f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.PIG, Items.PORKCHOP, BaseModGenes.MEATY, 1f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.ENDERMAN, Items.ENDER_PEARL, BaseModGenes.TELEPORT, 0.45f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.ENDERMAN, Items.GOLDEN_APPLE, BaseModGenes.MORE_HEARTS, 0.2f)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),
        GmoRecipeBuilder(EntityType.MOOSHROOM, Items.MUSHROOM_STEM, BaseModGenes.PHOTOSYNTHESIS, 0.7f)
            .unlockedBy("has_cell", has(ModItems.CELL.get()))
    )

    val mutationRecipes = listOf(
        GmoRecipeBuilder(EntityType.ENDER_DRAGON, Items.ELYTRA, BaseModGenes.FLIGHT, 0.55f, isMutation = true)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(
            EntityType.POLAR_BEAR,
            Items.NETHERITE_SWORD,
            BaseModGenes.STRENGTH_TWO,
            0.5f,
            isMutation = true
        ).unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(
            EntityType.SHULKER,
            Items.NETHERITE_CHESTPLATE,
            BaseModGenes.RESISTANCE_TWO,
            0.5f,
            isMutation = true
        ).unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(EntityType.POLAR_BEAR, Items.DIAMOND_SWORD, BaseModGenes.CLAWS_TWO, 0.75f, isMutation = true)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(EntityType.RABBIT, Items.DIAMOND_BOOTS, BaseModGenes.SPEED_TWO, 0.5f, isMutation = true)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(EntityType.OCELOT, Items.NETHERITE_BOOTS, BaseModGenes.SPEED_FOUR, 0.5f, isMutation = true)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(EntityType.RABBIT, Items.NETHERITE_PICKAXE, BaseModGenes.HASTE_TWO, 0.35f, isMutation = true)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(
            EntityType.SILVERFISH,
            Items.NETHERITE_PICKAXE,
            BaseModGenes.EFFICIENCY_FOUR,
            0.25f,
            isMutation = true
        ).unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(
            EntityType.ZOMBIE,
            Items.FERMENTED_SPIDER_EYE,
            BaseModGenes.SCARE_ZOMBIES,
            0.5f,
            isMutation = true
        ).unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(
            EntityType.SPIDER,
            Items.FERMENTED_SPIDER_EYE,
            BaseModGenes.SCARE_SPIDERS,
            0.5f,
            isMutation = true
        ).unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(
            EntityType.ENDER_DRAGON,
            Items.ENCHANTED_GOLDEN_APPLE,
            BaseModGenes.REGENERATION_FOUR,
            0.35f,
            isMutation = true
        ).unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(EntityType.PIG, Items.BLAZE_POWDER, BaseModGenes.MEATY_TWO, 0.75f, isMutation = true)
            .unlockedBy("has_cell", has(ModItems.CELL.get())),

        GmoRecipeBuilder(EntityType.ENDERMAN, Items.GOLDEN_APPLE, BaseModGenes.MORE_HEARTS_TWO, 0.25f, true)
            .unlockedBy("has_cell", has(ModItems.CELL.get()))
    )

    private val setPotionEntity =
        SingletonRecipeBuilder(
            SetPotionEntityRecipe.INSTANCE,
            Items.POTION,
            "incubator/set_potion_entity"
        ).unlockedBy("has_cell", has(ModItems.CELL.get()))

    private val blackDeath =
        SingletonRecipeBuilder(
            BlackDeathRecipe.INSTANCE,
            ModItems.DNA_HELIX.get(),
            "incubator/black_death"
        ).unlockedBy("has_cell", has(ModItems.CELL.get()))

    private val dupeCell = DupeCellRecipeBuilder()
        .unlockedBy("has_cell", has(ModItems.CELL.get()))

    private val dupeGmoCell = DupeCellRecipeBuilder(true)
        .unlockedBy("has_gmo_cell", has(ModItems.GMO_CELL.get()))

    val virusRecipes = listOf(
        VirusRecipeBuilder(BaseModGenes.POISON_IMMUNITY, BaseModGenes.POISON),
        VirusRecipeBuilder(BaseModGenes.WITHER_HIT, BaseModGenes.POISON_FOUR),
        VirusRecipeBuilder(BaseModGenes.WITHER_PROOF, BaseModGenes.WITHER),
        VirusRecipeBuilder(BaseModGenes.STRENGTH, BaseModGenes.WEAKNESS),
        VirusRecipeBuilder(BaseModGenes.NIGHT_VISION, BaseModGenes.BLINDNESS),
        VirusRecipeBuilder(BaseModGenes.SPEED, BaseModGenes.SLOWNESS),
        VirusRecipeBuilder(BaseModGenes.SPEED_TWO, BaseModGenes.SLOWNESS_FOUR),
        VirusRecipeBuilder(BaseModGenes.SPEED_FOUR, BaseModGenes.SLOWNESS_SIX),
        VirusRecipeBuilder(BaseModGenes.MILKY, BaseModGenes.NAUSEA),
        VirusRecipeBuilder(BaseModGenes.MEATY, BaseModGenes.NAUSEA),
        VirusRecipeBuilder(BaseModGenes.LAY_EGG, BaseModGenes.NAUSEA),
        VirusRecipeBuilder(BaseModGenes.NO_HUNGER, BaseModGenes.HUNGER),
        VirusRecipeBuilder(BaseModGenes.FIRE_PROOF, BaseModGenes.FLAMBE),
        VirusRecipeBuilder(BaseModGenes.LUCK, BaseModGenes.CURSED),
        VirusRecipeBuilder(BaseModGenes.HASTE, BaseModGenes.MINING_FATIGUE),
        VirusRecipeBuilder(BaseModGenes.SCARE_CREEPERS, BaseModGenes.GREEN_DEATH),
        VirusRecipeBuilder(BaseModGenes.SCARE_SKELETONS, BaseModGenes.UN_UNDEATH),
        VirusRecipeBuilder(BaseModGenes.SCARE_ZOMBIES, BaseModGenes.UN_UNDEATH),
        VirusRecipeBuilder(BaseModGenes.RESISTANCE, BaseModGenes.GRAY_DEATH),
        VirusRecipeBuilder(BaseModGenes.DRAGON_BREATH, BaseModGenes.WHITE_DEATH)
    )

}