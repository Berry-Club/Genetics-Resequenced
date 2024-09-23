package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.brewing.BrewingRecipe
import net.neoforged.neoforge.common.brewing.IBrewingRecipe
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

object BrewingRecipes {

    private val modPotions: List<Potion>
        get() = ModPotions.POTION_REGISTRY.entries.map { it.get() }

    val allRecipes: MutableList<IBrewingRecipe> = mutableListOf()

    fun tooltip(event: ItemTooltipEvent) {
        val stack = event.itemStack
        val itemPotion = OtherUtil.getPotion(stack) ?: return

        if (itemPotion == ModPotions.ZOMBIFY_VILLAGER || itemPotion == ModPotions.PANACEA) return
        if (itemPotion.value() !in modPotions) return

        if (stack.item != Items.POTION) {
            event.toolTip.add(
                ModLanguageProvider.Tooltips.IGNORE_POTION
                    .toComponent()
                    .withStyle { it.withColor(ChatFormatting.RED) }
            )
        }

        val itemGeneHolder = DnaHelixItem.getGeneHolder(stack)
        if (itemGeneHolder != null) {
            event.toolTip.add(
                ModLanguageProvider.Tooltips.GENE
                    .toComponent(Gene.getNameComponent(itemGeneHolder)
                        .withStyle { it.withColor(ChatFormatting.GRAY) }
                    ))
        }

        val itemEntity = EntityDnaItem.getEntityType(stack)
        if (itemEntity != null) {
            event.toolTip.add(
                ModLanguageProvider.Tooltips.HELIX_ENTITY
                    .toComponent(itemEntity.description)
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
        }

    }

    private fun ingredient(potion: Holder<Potion>): Ingredient =
        DataComponentIngredient.of(false, OtherUtil.getPotionStack(potion))

    private fun ingredient(itemLike: ItemLike): Ingredient = Ingredient.of(itemLike)
    private fun ingredient(itemStack: ItemStack): Ingredient = DataComponentIngredient.of(false, itemStack)

    val substratePotionStack
        get() = OtherUtil.getPotionStack(ModPotions.SUBSTRATE)
    val cellGrowthPotionStack
        get() = OtherUtil.getPotionStack(ModPotions.CELL_GROWTH)
    val mutationPotionStack
        get() = OtherUtil.getPotionStack(ModPotions.MUTATION)
    val viralAgentsPotionStack
        get() = OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)
    val panaceaPotionStack
        get() = OtherUtil.getPotionStack(ModPotions.PANACEA)

    fun setRecipes(event: RegisterBrewingRecipesEvent) {
        allRecipes.clear()

        val substrateRecipe = BrewingRecipe(
            ingredient(Potions.MUNDANE),
            ingredient(ModItems.ORGANIC_MATTER),
            substratePotionStack
        )
        val cellGrowthRecipe = BrewingRecipe(
            ingredient(ModPotions.SUBSTRATE),
            ingredient(DnaHelixItem.setBasic(ModItems.DNA_HELIX.toStack(), GeneticsResequenced.registryAccess!!)),
            cellGrowthPotionStack
        )
        val mutationRecipe = BrewingRecipe(
            ingredient(ModPotions.CELL_GROWTH),
            ingredient(Items.FERMENTED_SPIDER_EYE),
            mutationPotionStack
        )
        val viralRecipe = BrewingRecipe(
            ingredient(ModPotions.MUTATION),
            ingredient(Items.CHORUS_FRUIT),
            viralAgentsPotionStack
        )
        val panaceaRecipe = BrewingRecipe(
            ingredient(ModPotions.VIRAL_AGENTS),
            ingredient(
                DnaHelixItem.setGeneHolder(
                    ModItems.DNA_HELIX.toStack(),
                    ModGenes.REGENERATION.getHolder(GeneticsResequenced.registryAccess!!)!!
                )
            ),
            panaceaPotionStack
        )

        allRecipes.addAll(
            listOf(
                substrateRecipe,
                cellGrowthRecipe,
                mutationRecipe,
                viralRecipe,
                panaceaRecipe
            )
        )

        val substrateDuplicationRecipe = SubstrateCellRecipe()
        allRecipes.add(substrateDuplicationRecipe)
        val substrateGmoDuplicationRecipe = SubstrateCellRecipe(true)
        allRecipes.add(substrateGmoDuplicationRecipe)

        val setPcgEntityRecipe = SetPotionEntityRecipe()
        allRecipes.add(setPcgEntityRecipe)

        val geneFocusBrews = listOf(
            GmoRecipe(EntityType.BLAZE, Items.GLOWSTONE_DUST, ModGenes.BIOLUMINESCENCE, 0.85f),
            GmoRecipe(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, ModGenes.BIOLUMINESCENCE, 0.85f),
            GmoRecipe(EntityType.VILLAGER, Items.EMERALD, ModGenes.EMERALD_HEART, 0.85f),
            GmoRecipe(EntityType.SHULKER, Items.EMERALD_BLOCK, ModGenes.KEEP_INVENTORY, 0.45f),
            GmoRecipe(EntityType.RABBIT, Items.GOLDEN_BOOTS, ModGenes.SPEED, 0.65f),
            GmoRecipe(EntityType.RABBIT, Items.EMERALD, ModGenes.LUCK, 0.75f),
            GmoRecipe(EntityType.IRON_GOLEM, Items.GOLDEN_APPLE, ModGenes.REGENERATION, 0.3f),
            GmoRecipe(EntityType.CHICKEN, Items.EGG, ModGenes.LAY_EGG, 1f),
            GmoRecipe(EntityType.PIG, Items.PORKCHOP, ModGenes.MEATY, 1f),
            GmoRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL, ModGenes.TELEPORT, 0.45f),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.MORE_HEARTS, 0.2f),
            GmoRecipe(EntityType.MOOSHROOM, Items.MUSHROOM_STEM, ModGenes.PHOTOSYNTHESIS, 0.7f),
        )
        allRecipes.addAll(geneFocusBrews)

        val mutationBrews = listOf(
            GmoRecipe(EntityType.ENDER_DRAGON, Items.ELYTRA, ModGenes.FLIGHT, 0.55f, isMutation = true),
            GmoRecipe(
                EntityType.POLAR_BEAR,
                Items.NETHERITE_SWORD,
                ModGenes.STRENGTH_TWO,
                0.5f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.SHULKER,
                Items.NETHERITE_CHESTPLATE,
                ModGenes.RESISTANCE_TWO,
                0.5f,
                isMutation = true
            ),
            GmoRecipe(EntityType.POLAR_BEAR, Items.DIAMOND_SWORD, ModGenes.CLAWS_TWO, 0.75f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.DIAMOND_BOOTS, ModGenes.SPEED_TWO, 0.5f, isMutation = true),
            GmoRecipe(EntityType.OCELOT, Items.NETHERITE_BOOTS, ModGenes.SPEED_FOUR, 0.5f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.NETHERITE_PICKAXE, ModGenes.HASTE_TWO, 0.35f, isMutation = true),
            GmoRecipe(
                EntityType.SILVERFISH,
                Items.NETHERITE_PICKAXE,
                ModGenes.EFFICIENCY_FOUR,
                0.25f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.ZOMBIE,
                Items.FERMENTED_SPIDER_EYE,
                ModGenes.SCARE_ZOMBIES,
                0.5f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.SPIDER,
                Items.FERMENTED_SPIDER_EYE,
                ModGenes.SCARE_SPIDERS,
                0.5f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.ENDER_DRAGON,
                Items.ENCHANTED_GOLDEN_APPLE,
                ModGenes.REGENERATION_FOUR,
                0.35f,
                isMutation = true
            ),
            GmoRecipe(EntityType.PIG, Items.BLAZE_POWDER, ModGenes.MEATY_TWO, 0.75f, isMutation = true),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.MORE_HEARTS_TWO, 0.25f, true)
        )
        allRecipes.addAll(mutationBrews)

        val virusBrews = listOf(
            VirusRecipe(ModGenes.POISON_IMMUNITY, ModGenes.POISON),
            VirusRecipe(ModGenes.WITHER_HIT, ModGenes.POISON_FOUR),
            VirusRecipe(ModGenes.WITHER_PROOF, ModGenes.WITHER),
            VirusRecipe(ModGenes.STRENGTH, ModGenes.WEAKNESS),
            VirusRecipe(ModGenes.NIGHT_VISION, ModGenes.BLINDNESS),
            VirusRecipe(ModGenes.SPEED, ModGenes.SLOWNESS),
            VirusRecipe(ModGenes.SPEED_TWO, ModGenes.SLOWNESS_FOUR),
            VirusRecipe(ModGenes.SPEED_FOUR, ModGenes.SLOWNESS_SIX),
            VirusRecipe(ModGenes.MILKY, ModGenes.NAUSEA),
            VirusRecipe(ModGenes.MEATY, ModGenes.NAUSEA),
            VirusRecipe(ModGenes.LAY_EGG, ModGenes.NAUSEA),
            VirusRecipe(ModGenes.NO_HUNGER, ModGenes.HUNGER),
            VirusRecipe(ModGenes.FIRE_PROOF, ModGenes.FLAMBE),
            VirusRecipe(ModGenes.LUCK, ModGenes.CURSED),
            VirusRecipe(ModGenes.HASTE, ModGenes.MINING_FATIGUE),
            VirusRecipe(ModGenes.SCARE_CREEPERS, ModGenes.GREEN_DEATH),
            VirusRecipe(ModGenes.SCARE_SKELETONS, ModGenes.UN_UNDEATH),
            VirusRecipe(ModGenes.SCARE_ZOMBIES, ModGenes.UN_UNDEATH),
            VirusRecipe(ModGenes.RESISTANCE, ModGenes.GRAY_DEATH),
            VirusRecipe(ModGenes.DRAGON_BREATH, ModGenes.WHITE_DEATH),

            BlackDeathRecipe(),

            BrewingRecipe(
                ingredient(viralAgentsPotionStack),
                ingredient(
                    DnaHelixItem.setGeneHolder(
                        ModItems.DNA_HELIX.toStack(),
                        ModGenes.EMERALD_HEART.getHolder(GeneticsResequenced.registryAccess!!)!!
                    )
                ),
                OtherUtil.getPotionStack(ModPotions.ZOMBIFY_VILLAGER)
            )
        )
        allRecipes.addAll(virusBrews)

        for (recipe in allRecipes) {
            event.builder.addRecipe(recipe)
        }

    }

}