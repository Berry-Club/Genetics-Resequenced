package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
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

    private val modPotions: List<Potion> by lazy {
        ModPotions.POTION_REGISTRY.entries.map { it.get() }
    }

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

        val itemGene = DnaHelixItem.getGene(stack)
        if (itemGene != null) {
            event.toolTip.add(
                ModLanguageProvider.Tooltips.GENE
                    .toComponent(itemGene.nameComponent)
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
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

    val substratePotionStack by lazy { OtherUtil.getPotionStack(ModPotions.SUBSTRATE) }
    val cellGrowthPotionStack by lazy { OtherUtil.getPotionStack(ModPotions.CELL_GROWTH) }
    val mutationPotionStack by lazy { OtherUtil.getPotionStack(ModPotions.MUTATION) }
    val viralAgentsPotionStack by lazy { OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS) }
    val curePotionStack by lazy { OtherUtil.getPotionStack(ModPotions.PANACEA) }

    fun addRecipes(event: RegisterBrewingRecipesEvent) {

        val substrateRecipe = BrewingRecipe(
            ingredient(Potions.MUNDANE),
            ingredient(ModItems.ORGANIC_MATTER),
            substratePotionStack
        )
        val cellGrowthRecipe = BrewingRecipe(
            ingredient(ModPotions.SUBSTRATE),
            ingredient(DnaHelixItem.setBasic(ModItems.DNA_HELIX.toStack())),
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
            ingredient(DnaHelixItem.setGene(ModItems.DNA_HELIX.toStack(), ModGenes.EMERALD_HEART)),
            curePotionStack
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

        val setPcgEntityRecipe = SetPotionEntityRecipe()
        allRecipes.add(setPcgEntityRecipe)

        val geneFocusBrews = listOf(
            GmoRecipe(EntityType.BLAZE, Items.GLOWSTONE_DUST, ModGenes.BIOLUMINESCENCE.get(), 0.85f),
            GmoRecipe(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, ModGenes.BIOLUMINESCENCE.get(), 0.85f),
            GmoRecipe(EntityType.VILLAGER, Items.EMERALD, ModGenes.EMERALD_HEART.get(), 0.85f),
            GmoRecipe(EntityType.SHULKER, Items.EMERALD_BLOCK, ModGenes.KEEP_INVENTORY.get(), 0.45f),
            GmoRecipe(EntityType.RABBIT, Items.GOLDEN_BOOTS, ModGenes.SPEED.get(), 0.65f),
            GmoRecipe(EntityType.RABBIT, Items.EMERALD, ModGenes.LUCK.get(), 0.75f),
            GmoRecipe(EntityType.IRON_GOLEM, Items.GOLDEN_APPLE, ModGenes.REGENERATION.get(), 0.3f),
            GmoRecipe(EntityType.CHICKEN, Items.EGG, ModGenes.LAY_EGG.get(), 1f),
            GmoRecipe(EntityType.PIG, Items.PORKCHOP, ModGenes.MEATY.get(), 1f),
            GmoRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL, ModGenes.TELEPORT.get(), 0.45f),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.MORE_HEARTS.get(), 0.2f),
            GmoRecipe(EntityType.MOOSHROOM, Items.MUSHROOM_STEM, ModGenes.PHOTOSYNTHESIS.get(), 0.7f),
        )
        allRecipes.addAll(geneFocusBrews)

        val mutationBrews = listOf(
            GmoRecipe(EntityType.ENDER_DRAGON, Items.ELYTRA, ModGenes.FLIGHT.get(), 0.55f, isMutation = true),
            GmoRecipe(
                EntityType.POLAR_BEAR,
                Items.NETHERITE_SWORD,
                ModGenes.STRENGTH_TWO.get(),
                0.5f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.SHULKER,
                Items.NETHERITE_CHESTPLATE,
                ModGenes.RESISTANCE_TWO.get(),
                0.5f,
                isMutation = true
            ),
            GmoRecipe(EntityType.POLAR_BEAR, Items.DIAMOND_SWORD, ModGenes.CLAWS_TWO.get(), 0.75f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.DIAMOND_BOOTS, ModGenes.SPEED_TWO.get(), 0.5f, isMutation = true),
            GmoRecipe(EntityType.OCELOT, Items.NETHERITE_BOOTS, ModGenes.SPEED_FOUR.get(), 0.5f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.NETHERITE_PICKAXE, ModGenes.HASTE_TWO.get(), 0.35f, isMutation = true),
            GmoRecipe(
                EntityType.SILVERFISH,
                Items.NETHERITE_PICKAXE,
                ModGenes.EFFICIENCY_FOUR.get(),
                0.25f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.ZOMBIE,
                Items.FERMENTED_SPIDER_EYE,
                ModGenes.SCARE_ZOMBIES.get(),
                0.5f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.SPIDER,
                Items.FERMENTED_SPIDER_EYE,
                ModGenes.SCARE_SPIDERS.get(),
                0.5f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.ENDER_DRAGON,
                Items.ENCHANTED_GOLDEN_APPLE,
                ModGenes.REGENERATION_FOUR.get(),
                0.35f,
                isMutation = true
            ),
            GmoRecipe(EntityType.PIG, Items.BLAZE_POWDER, ModGenes.MEATY_TWO.get(), 0.75f, isMutation = true),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.MORE_HEARTS_TWO.get(), 0.25f, true)
        )
        allRecipes.addAll(mutationBrews)

        val virusBrews = listOf(
            VirusRecipe(ModGenes.POISON_IMMUNITY.get(), ModGenes.POISON.get()),
            VirusRecipe(ModGenes.WITHER_HIT.get(), ModGenes.POISON_FOUR.get()),
            VirusRecipe(ModGenes.WITHER_PROOF.get(), ModGenes.WITHER.get()),
            VirusRecipe(ModGenes.STRENGTH.get(), ModGenes.WEAKNESS.get()),
            VirusRecipe(ModGenes.NIGHT_VISION.get(), ModGenes.BLINDNESS.get()),
            VirusRecipe(ModGenes.SPEED.get(), ModGenes.SLOWNESS.get()),
            VirusRecipe(ModGenes.SPEED_TWO.get(), ModGenes.SLOWNESS_FOUR.get()),
            VirusRecipe(ModGenes.SPEED_FOUR.get(), ModGenes.SLOWNESS_SIX.get()),
            VirusRecipe(ModGenes.MILKY.get(), ModGenes.NAUSEA.get()),
            VirusRecipe(ModGenes.MEATY.get(), ModGenes.NAUSEA.get()),
            VirusRecipe(ModGenes.LAY_EGG.get(), ModGenes.NAUSEA.get()),
            VirusRecipe(ModGenes.NO_HUNGER.get(), ModGenes.HUNGER.get()),
            VirusRecipe(ModGenes.FIRE_PROOF.get(), ModGenes.FLAMBE.get()),
            VirusRecipe(ModGenes.LUCK.get(), ModGenes.CURSED.get()),
            VirusRecipe(ModGenes.HASTE.get(), ModGenes.MINING_FATIGUE.get()),
            VirusRecipe(ModGenes.SCARE_CREEPERS.get(), ModGenes.GREEN_DEATH.get()),
            VirusRecipe(ModGenes.SCARE_SKELETONS.get(), ModGenes.UN_UNDEATH.get()),
            VirusRecipe(ModGenes.SCARE_ZOMBIES.get(), ModGenes.UN_UNDEATH.get()),
            VirusRecipe(ModGenes.RESISTANCE.get(), ModGenes.GRAY_DEATH.get()),
            VirusRecipe(ModGenes.DRAGON_BREATH.get(), ModGenes.WHITE_DEATH.get()),

            BlackDeathRecipe(),

            BrewingRecipe(
                ingredient(viralAgentsPotionStack),
                ingredient(DnaHelixItem.setGene(ModItems.DNA_HELIX.toStack(), ModGenes.EMERALD_HEART)),
                OtherUtil.getPotionStack(ModPotions.ZOMBIFY_VILLAGER)
            )
        )
        allRecipes.addAll(virusBrews)

        for (recipe in allRecipes) {
            event.builder.addRecipe(recipe)
        }

    }

}