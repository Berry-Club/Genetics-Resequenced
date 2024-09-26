package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types.*
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BlackDeathRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.SetPotionEntityRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    val lookupProvider: CompletableFuture<HolderLookup.Provider>
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

        for (entityGeneWeightRecipe in entityGeneWeightRecipes.flatten()) {
            entityGeneWeightRecipe.save(pRecipeOutput)
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

    private val basicIncubatorRecipe = listOf(
        BasicIncubatorRecipeBuilder(
            DataComponentIngredient.of(
                false,
                DnaHelixItem.getHelixStack(ModGenes.BASIC, lookupProvider.get())
            ),
            DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.SUBSTRATE)),
            OtherUtil.getPotionStack(ModPotions.CELL_GROWTH),
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

    private fun entityGeneWeights(
        entityType: EntityType<*>,
        map: Map<ResourceKey<Gene>, Int>
    ): List<EntityGeneWeightRecipeBuilder> {
        val recipes = mutableListOf<EntityGeneWeightRecipeBuilder>()

        for ((gene, weight) in map) {
            recipes.add(EntityGeneWeightRecipeBuilder(entityType, gene.getHolder(lookupProvider.get())!!, weight))
        }

        return recipes.toList()
    }

    private val entityGeneWeightRecipes = listOf(
        entityGeneWeights(EntityType.ALLAY, mapOf(ModGenes.BASIC to 3, ModGenes.ITEM_MAGNET to 5)),
        entityGeneWeights(EntityType.AXOLOTL, mapOf(ModGenes.BASIC to 2, ModGenes.WATER_BREATHING to 5)),
        entityGeneWeights(
            EntityType.BAT,
            mapOf(ModGenes.BASIC to 4, ModGenes.NIGHT_VISION to 1, ModGenes.MOB_SIGHT to 3)
        ),
        entityGeneWeights(EntityType.BEE, mapOf(ModGenes.BASIC to 5, ModGenes.THORNS to 3)),
        entityGeneWeights(
            EntityType.BLAZE,
            mapOf(
                ModGenes.BASIC to 5,
                ModGenes.SHOOT_FIREBALLS to 3,
                ModGenes.FIRE_PROOF to 1,
                ModGenes.BIOLUMINESCENCE to 3
            )
        ),
        entityGeneWeights(
            EntityType.BREEZE,
            mapOf(ModGenes.BASIC to 2, ModGenes.JUMP_BOOST to 5, ModGenes.WIND_CHARGED to 4)
        ),
        entityGeneWeights(EntityType.CAT, mapOf(ModGenes.BASIC to 5, ModGenes.SCARE_CREEPERS to 2)),
        entityGeneWeights(
            EntityType.CAVE_SPIDER,
            mapOf(
                ModGenes.BASIC to 7,
                ModGenes.NIGHT_VISION to 5,
                ModGenes.WALL_CLIMBING to 2,
                ModGenes.POISON_IMMUNITY to 1
            )
        ),
        entityGeneWeights(
            EntityType.CHICKEN,
            mapOf(ModGenes.BASIC to 5, ModGenes.NO_FALL_DAMAGE to 1, ModGenes.LAY_EGG to 4)
        ),
        entityGeneWeights(EntityType.COD, mapOf(ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 2)),
        entityGeneWeights(EntityType.COW, mapOf(ModGenes.BASIC to 5, ModGenes.EAT_GRASS to 3, ModGenes.MILKY to 4)),
        entityGeneWeights(EntityType.CREEPER, mapOf(ModGenes.BASIC to 5, ModGenes.EXPLOSIVE_EXIT to 3)),
        entityGeneWeights(
            EntityType.DOLPHIN,
            mapOf(ModGenes.BASIC to 7, ModGenes.SPEED to 2, ModGenes.JUMP_BOOST to 2)
        ),
        entityGeneWeights(
            EntityType.DONKEY,
            mapOf(ModGenes.BASIC to 9, ModGenes.JUMP_BOOST to 3, ModGenes.STEP_ASSIST to 3, ModGenes.KNOCKBACK to 3)
        ),
        entityGeneWeights(EntityType.DROWNED, mapOf(ModGenes.BASIC to 5, ModGenes.RESISTANCE to 4)),
        entityGeneWeights(EntityType.ELDER_GUARDIAN, mapOf(ModGenes.MOB_SIGHT to 3, ModGenes.WATER_BREATHING to 4)),
        entityGeneWeights(
            EntityType.ENDER_DRAGON,
            mapOf(
                ModGenes.BASIC to 5,
                ModGenes.DRAGON_BREATH to 6,
                ModGenes.ENDER_DRAGON_HEALTH to 3,
                ModGenes.FLIGHT to 2
            )
        ),
        entityGeneWeights(
            EntityType.ENDERMAN,
            mapOf(ModGenes.BASIC to 5, ModGenes.MORE_HEARTS to 1, ModGenes.TELEPORT to 3)
        ),
        entityGeneWeights(
            EntityType.ENDERMITE,
            mapOf(ModGenes.KEEP_INVENTORY to 1, ModGenes.ITEM_MAGNET to 2, ModGenes.XP_MAGNET to 3)
        ),
        entityGeneWeights(
            EntityType.EVOKER,
            mapOf(ModGenes.BASIC to 4, ModGenes.EMERALD_HEART to 3, ModGenes.BAD_OMEN to 4)
        ),
        entityGeneWeights(EntityType.FOX, mapOf(ModGenes.BASIC to 5, ModGenes.SPEED to 2, ModGenes.JUMP_BOOST to 2)),
        entityGeneWeights(EntityType.FROG, mapOf(ModGenes.BASIC to 5, ModGenes.JUMP_BOOST to 3)),
        entityGeneWeights(EntityType.GHAST, mapOf(ModGenes.BASIC to 5, ModGenes.SHOOT_FIREBALLS to 4)),
        entityGeneWeights(
            EntityType.GLOW_SQUID,
            mapOf(ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 4, ModGenes.BIOLUMINESCENCE to 4)
        ),
        entityGeneWeights(
            EntityType.GOAT,
            mapOf(ModGenes.BASIC to 5, ModGenes.EAT_GRASS to 3, ModGenes.WOOLY to 3, ModGenes.KNOCKBACK to 4)
        ),
        entityGeneWeights(
            EntityType.GUARDIAN,
            mapOf(ModGenes.BASIC to 5, ModGenes.MOB_SIGHT to 3, ModGenes.WATER_BREATHING to 4)
        ),
        entityGeneWeights(EntityType.HOGLIN, mapOf(ModGenes.BASIC to 5, ModGenes.MEATY to 5)),
        entityGeneWeights(
            EntityType.HORSE,
            mapOf(ModGenes.BASIC to 7, ModGenes.JUMP_BOOST to 3, ModGenes.STEP_ASSIST to 3, ModGenes.SPEED to 3)
        ),
        entityGeneWeights(EntityType.HUSK, mapOf(ModGenes.BASIC to 5, ModGenes.CHILLING to 4)),
        entityGeneWeights(
            EntityType.IRON_GOLEM,
            mapOf(
                ModGenes.BASIC to 5,
                ModGenes.MORE_HEARTS to 2,
                ModGenes.REGENERATION to 2,
                ModGenes.RESISTANCE to 5,
                ModGenes.STRENGTH to 3,
                ModGenes.REACHING to 3
            )
        ),
        entityGeneWeights(EntityType.LLAMA, mapOf(ModGenes.BASIC to 5, ModGenes.STEP_ASSIST to 3)),
        entityGeneWeights(
            EntityType.MAGMA_CUBE,
            mapOf(ModGenes.BASIC to 5, ModGenes.FIRE_PROOF to 3, ModGenes.BIOLUMINESCENCE to 4)
        ),
        entityGeneWeights(
            EntityType.MOOSHROOM,
            mapOf(ModGenes.EAT_GRASS to 4, ModGenes.MILKY to 4, ModGenes.PHOTOSYNTHESIS to 3)
        ),
        entityGeneWeights(
            EntityType.OCELOT,
            mapOf(ModGenes.BASIC to 5, ModGenes.SPEED to 2, ModGenes.SCARE_CREEPERS to 3)
        ),
        entityGeneWeights(
            EntityType.PARROT,
            mapOf(ModGenes.BASIC to 5, ModGenes.NO_FALL_DAMAGE to 2, ModGenes.LAY_EGG to 4, ModGenes.CHATTERBOX to 6)
        ),
        entityGeneWeights(
            EntityType.PHANTOM,
            mapOf(ModGenes.BASIC to 5, ModGenes.INVISIBLE to 3, ModGenes.TELEPORT to 3)
        ),
        entityGeneWeights(EntityType.PIG, mapOf(ModGenes.BASIC to 5, ModGenes.MEATY to 2)),
        entityGeneWeights(EntityType.PIGLIN_BRUTE, mapOf(ModGenes.BASIC to 5, ModGenes.MEATY to 4)),
        entityGeneWeights(EntityType.PIGLIN, mapOf(ModGenes.BASIC to 5, ModGenes.MEATY to 3)),
        entityGeneWeights(
            EntityType.PILLAGER,
            mapOf(ModGenes.BASIC to 5, ModGenes.EMERALD_HEART to 2, ModGenes.BAD_OMEN to 3)
        ),
        entityGeneWeights(EntityType.PLAYER, mapOf(ModGenes.CRINGE to Int.MAX_VALUE)),
        entityGeneWeights(
            EntityType.POLAR_BEAR,
            mapOf(ModGenes.BASIC to 5, ModGenes.STRENGTH to 4, ModGenes.CLAWS to 3, ModGenes.STEP_ASSIST to 4)
        ),
        entityGeneWeights(
            EntityType.PUFFERFISH,
            mapOf(ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 4, ModGenes.POISON to 3, ModGenes.THORNS to 4)
        ),
        entityGeneWeights(
            EntityType.RABBIT,
            mapOf(ModGenes.BASIC to 5, ModGenes.JUMP_BOOST to 5, ModGenes.SPEED to 3, ModGenes.LUCK to 4)
        ),
        entityGeneWeights(
            EntityType.RAVAGER,
            mapOf(ModGenes.BASIC to 5, ModGenes.STRENGTH to 5, ModGenes.RESISTANCE to 4, ModGenes.MORE_HEARTS to 3)
        ),
        entityGeneWeights(EntityType.SALMON, mapOf(ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 3)),
        entityGeneWeights(EntityType.SHEEP, mapOf(ModGenes.BASIC to 5, ModGenes.EAT_GRASS to 3, ModGenes.WOOLY to 4)),
        entityGeneWeights(
            EntityType.SHULKER,
            mapOf(ModGenes.BASIC to 3, ModGenes.RESISTANCE to 4, ModGenes.LEVITATION to 4)
        ),
        entityGeneWeights(
            EntityType.SILVERFISH,
            mapOf(ModGenes.BASIC to 3, ModGenes.HASTE to 3, ModGenes.EFFICIENCY to 1, ModGenes.INFESTED to 4)
        ),
        entityGeneWeights(
            EntityType.SKELETON_HORSE,
            mapOf(ModGenes.JUMP_BOOST to 4, ModGenes.STEP_ASSIST to 4, ModGenes.SPEED to 4)
        ),
        entityGeneWeights(EntityType.SKELETON, mapOf(ModGenes.BASIC to 5, ModGenes.INFINITY to 1)),
        entityGeneWeights(
            EntityType.SLIME,
            mapOf(ModGenes.BASIC to 5, ModGenes.NO_FALL_DAMAGE to 4, ModGenes.SLIMY_DEATH to 2, ModGenes.OOZING to 5)
        ),
        entityGeneWeights(EntityType.SNOW_GOLEM, mapOf(ModGenes.BASIC to 5, ModGenes.CHILLING to 2)),
        entityGeneWeights(
            EntityType.SPIDER,
            mapOf(ModGenes.BASIC to 4, ModGenes.NIGHT_VISION to 3, ModGenes.WALL_CLIMBING to 2, ModGenes.WEAVING to 4)
        ),
        entityGeneWeights(EntityType.SQUID, mapOf(ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 2)),
        entityGeneWeights(EntityType.STRAY, mapOf(ModGenes.BASIC to 5, ModGenes.INFINITY to 3)),
        entityGeneWeights(EntityType.STRIDER, mapOf(ModGenes.BASIC to 5, ModGenes.FIRE_PROOF to 4)),
        entityGeneWeights(ModEntityTypes.SUPPORT_SLIME.get(), mapOf(ModGenes.SLIMY_DEATH to 1)),
        entityGeneWeights(EntityType.TADPOLE, mapOf(ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 4)),
        entityGeneWeights(EntityType.TROPICAL_FISH, mapOf(ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 2)),
        entityGeneWeights(
            EntityType.TURTLE,
            mapOf(ModGenes.BASIC to 5, ModGenes.WATER_BREATHING to 2, ModGenes.RESISTANCE to 2)
        ),
        entityGeneWeights(EntityType.VILLAGER, mapOf(ModGenes.BASIC to 5, ModGenes.EMERALD_HEART to 2)),
        entityGeneWeights(
            EntityType.VINDICATOR,
            mapOf(ModGenes.BASIC to 5, ModGenes.EMERALD_HEART to 2, ModGenes.BAD_OMEN to 3, ModGenes.JOHNNY to 3)
        ),
        entityGeneWeights(EntityType.WARDEN, mapOf(ModGenes.MORE_HEARTS to 5, ModGenes.MOB_SIGHT to 5)),
        entityGeneWeights(EntityType.WITCH, mapOf(ModGenes.BASIC to 5, ModGenes.POISON_IMMUNITY to 2)),
        entityGeneWeights(EntityType.WITHER_SKELETON, mapOf(ModGenes.BASIC to 5, ModGenes.WITHER_HIT to 2)),
        entityGeneWeights(
            EntityType.WITHER,
            mapOf(ModGenes.BASIC to 5, ModGenes.WITHER_PROOF to 3, ModGenes.WITHER_HIT to 4, ModGenes.FLIGHT to 3)
        ),
        entityGeneWeights(
            EntityType.WOLF,
            mapOf(
                ModGenes.BASIC to 5,
                ModGenes.SCARE_SKELETONS to 2,
                ModGenes.NIGHT_VISION to 3,
                ModGenes.NO_HUNGER to 1
            )
        ),
        entityGeneWeights(EntityType.ZOGLIN, mapOf(ModGenes.BASIC to 5, ModGenes.MEATY to 4)),
        entityGeneWeights(
            EntityType.ZOMBIE_HORSE,
            mapOf(ModGenes.JUMP_BOOST to 4, ModGenes.STEP_ASSIST to 4, ModGenes.SPEED to 4)
        ),
        entityGeneWeights(EntityType.ZOMBIE_VILLAGER, mapOf(ModGenes.BASIC to 5, ModGenes.EMERALD_HEART to 4)),
        entityGeneWeights(EntityType.ZOMBIE, mapOf(ModGenes.BASIC to 7, ModGenes.RESISTANCE to 1)),
        entityGeneWeights(
            EntityType.ZOMBIFIED_PIGLIN,
            mapOf(ModGenes.BASIC to 5, ModGenes.FIRE_PROOF to 3, ModGenes.MEATY to 3)
        ),
    )

}