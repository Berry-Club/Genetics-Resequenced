package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
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
import net.neoforged.neoforge.registries.DeferredHolder

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

        val itemGeneHolder = DnaHelixItem.getGene(stack, ClientUtil.localRegistryAccess!!)
        if (itemGeneHolder != null) {
            event.toolTip.add(
                ModLanguageProvider.Tooltips.GENE
                    .toComponent(itemGeneHolder.value().nameComponent(ClientUtil.localRegistryAccess!!))
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

    fun defer(geneRk: ResourceKey<Gene>): DeferredHolder<Gene, Gene> {
        return DeferredHolder.create(geneRk)
    }

    fun setRecipes(event: RegisterBrewingRecipesEvent) {
        allRecipes.clear()

        val substrateRecipe = BrewingRecipe(
            ingredient(Potions.MUNDANE),
            ingredient(ModItems.ORGANIC_MATTER),
            substratePotionStack
        )
        val cellGrowthRecipe = BrewingRecipe(
            ingredient(ModPotions.SUBSTRATE),
            ingredient(DnaHelixItem.setGeneRk(ModItems.DNA_HELIX.toStack(), defer(ModGenes.BASIC))),
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
            ingredient(DnaHelixItem.setGeneRk(ModItems.DNA_HELIX.toStack(), defer(ModGenes.REGENERATION))),
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
            GmoRecipe(EntityType.BLAZE, Items.GLOWSTONE_DUST, defer(ModGenes.BIOLUMINESCENCE), 0.85f),
            GmoRecipe(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, defer(ModGenes.BIOLUMINESCENCE), 0.85f),
            GmoRecipe(EntityType.VILLAGER, Items.EMERALD, defer(ModGenes.EMERALD_HEART), 0.85f),
            GmoRecipe(EntityType.SHULKER, Items.EMERALD_BLOCK, defer(ModGenes.KEEP_INVENTORY), 0.45f),
            GmoRecipe(EntityType.RABBIT, Items.GOLDEN_BOOTS, defer(ModGenes.SPEED), 0.65f),
            GmoRecipe(EntityType.RABBIT, Items.EMERALD, defer(ModGenes.LUCK), 0.75f),
            GmoRecipe(EntityType.IRON_GOLEM, Items.GOLDEN_APPLE, defer(ModGenes.REGENERATION), 0.3f),
            GmoRecipe(EntityType.CHICKEN, Items.EGG, defer(ModGenes.LAY_EGG), 1f),
            GmoRecipe(EntityType.PIG, Items.PORKCHOP, defer(ModGenes.MEATY), 1f),
            GmoRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL, defer(ModGenes.TELEPORT), 0.45f),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, defer(ModGenes.MORE_HEARTS), 0.2f),
            GmoRecipe(EntityType.MOOSHROOM, Items.MUSHROOM_STEM, defer(ModGenes.PHOTOSYNTHESIS), 0.7f),
        )
        allRecipes.addAll(geneFocusBrews)

        val mutationBrews = listOf(
            GmoRecipe(EntityType.ENDER_DRAGON, Items.ELYTRA, defer(ModGenes.FLIGHT), 0.55f, isMutation = true),
            GmoRecipe(
                EntityType.POLAR_BEAR,
                Items.NETHERITE_SWORD,
                defer(ModGenes.STRENGTH_TWO),
                0.5f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.SHULKER,
                Items.NETHERITE_CHESTPLATE,
                defer(ModGenes.RESISTANCE_TWO),
                0.5f,
                isMutation = true
            ),
            GmoRecipe(EntityType.POLAR_BEAR, Items.DIAMOND_SWORD, defer(ModGenes.CLAWS_TWO), 0.75f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.DIAMOND_BOOTS, defer(ModGenes.SPEED_TWO), 0.5f, isMutation = true),
            GmoRecipe(EntityType.OCELOT, Items.NETHERITE_BOOTS, defer(ModGenes.SPEED_FOUR), 0.5f, isMutation = true),
            GmoRecipe(EntityType.RABBIT, Items.NETHERITE_PICKAXE, defer(ModGenes.HASTE_TWO), 0.35f, isMutation = true),
            GmoRecipe(
                EntityType.SILVERFISH,
                Items.NETHERITE_PICKAXE,
                defer(ModGenes.EFFICIENCY_FOUR),
                0.25f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.ZOMBIE,
                Items.FERMENTED_SPIDER_EYE,
                defer(ModGenes.SCARE_ZOMBIES),
                0.5f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.SPIDER,
                Items.FERMENTED_SPIDER_EYE,
                defer(ModGenes.SCARE_SPIDERS),
                0.5f,
                isMutation = true
            ),
            GmoRecipe(
                EntityType.ENDER_DRAGON,
                Items.ENCHANTED_GOLDEN_APPLE,
                defer(ModGenes.REGENERATION_FOUR),
                0.35f,
                isMutation = true
            ),
            GmoRecipe(EntityType.PIG, Items.BLAZE_POWDER, defer(ModGenes.MEATY_TWO), 0.75f, isMutation = true),
            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, defer(ModGenes.MORE_HEARTS_TWO), 0.25f, true)
        )
        allRecipes.addAll(mutationBrews)

        val virusBrews = listOf(
            VirusRecipe(defer(ModGenes.POISON_IMMUNITY), defer(ModGenes.POISON)),
            VirusRecipe(defer(ModGenes.WITHER_HIT), defer(ModGenes.POISON_FOUR)),
            VirusRecipe(defer(ModGenes.WITHER_PROOF), defer(ModGenes.WITHER)),
            VirusRecipe(defer(ModGenes.STRENGTH), defer(ModGenes.WEAKNESS)),
            VirusRecipe(defer(ModGenes.NIGHT_VISION), defer(ModGenes.BLINDNESS)),
            VirusRecipe(defer(ModGenes.SPEED), defer(ModGenes.SLOWNESS)),
            VirusRecipe(defer(ModGenes.SPEED_TWO), defer(ModGenes.SLOWNESS_FOUR)),
            VirusRecipe(defer(ModGenes.SPEED_FOUR), defer(ModGenes.SLOWNESS_SIX)),
            VirusRecipe(defer(ModGenes.MILKY), defer(ModGenes.NAUSEA)),
            VirusRecipe(defer(ModGenes.MEATY), defer(ModGenes.NAUSEA)),
            VirusRecipe(defer(ModGenes.LAY_EGG), defer(ModGenes.NAUSEA)),
            VirusRecipe(defer(ModGenes.NO_HUNGER), defer(ModGenes.HUNGER)),
            VirusRecipe(defer(ModGenes.FIRE_PROOF), defer(ModGenes.FLAMBE)),
            VirusRecipe(defer(ModGenes.LUCK), defer(ModGenes.CURSED)),
            VirusRecipe(defer(ModGenes.HASTE), defer(ModGenes.MINING_FATIGUE)),
            VirusRecipe(defer(ModGenes.SCARE_CREEPERS), defer(ModGenes.GREEN_DEATH)),
            VirusRecipe(defer(ModGenes.SCARE_SKELETONS), defer(ModGenes.UN_UNDEATH)),
            VirusRecipe(defer(ModGenes.SCARE_ZOMBIES), defer(ModGenes.UN_UNDEATH)),
            VirusRecipe(defer(ModGenes.RESISTANCE), defer(ModGenes.GRAY_DEATH)),
            VirusRecipe(defer(ModGenes.DRAGON_BREATH), defer(ModGenes.WHITE_DEATH)),

            BlackDeathRecipe(),

            BrewingRecipe(
                ingredient(viralAgentsPotionStack),
                ingredient(DnaHelixItem.setGeneRk(ModItems.DNA_HELIX.toStack(), defer(ModGenes.EMERALD_HEART))),
                OtherUtil.getPotionStack(ModPotions.ZOMBIFY_VILLAGER)
            )
        )
        allRecipes.addAll(virusBrews)

        for (recipe in allRecipes) {
            event.builder.addRecipe(recipe)
        }

    }

}