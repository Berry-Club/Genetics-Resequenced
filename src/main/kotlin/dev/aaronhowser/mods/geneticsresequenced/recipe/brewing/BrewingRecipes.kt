package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
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
            ingredient(DnaHelixItem.setGene(ModItems.DNA_HELIX.toStack(), ModGenes.emeraldHeart)),
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
//
//        val geneFocusBrews = listOf(
//            GmoRecipe(EntityType.BLAZE, Items.GLOWSTONE_DUST, ModGenes.bioluminescence, 0.85f),
//            GmoRecipe(EntityType.MAGMA_CUBE, Items.GLOWSTONE_DUST, ModGenes.bioluminescence, 0.85f),
//            GmoRecipe(EntityType.VILLAGER, Items.EMERALD, ModGenes.emeraldHeart, 0.85f),
//            GmoRecipe(EntityType.SHULKER, Items.EMERALD_BLOCK, ModGenes.keepInventory, 0.45f),
//            GmoRecipe(EntityType.RABBIT, Items.GOLDEN_BOOTS, ModGenes.speed, 0.65f),
//            GmoRecipe(EntityType.RABBIT, Items.EMERALD, ModGenes.luck, 0.75f),
//            GmoRecipe(EntityType.IRON_GOLEM, Items.GOLDEN_APPLE, ModGenes.regeneration, 0.3f),
//            GmoRecipe(EntityType.CHICKEN, Items.EGG, ModGenes.layEgg, 1f),
//            GmoRecipe(EntityType.PIG, Items.PORKCHOP, ModGenes.meaty, 1f),
//            GmoRecipe(EntityType.ENDERMAN, Items.ENDER_PEARL, ModGenes.teleport, 0.45f),
//            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.moreHearts, 0.2f),
//            GmoRecipe(EntityType.MOOSHROOM, Items.MUSHROOM_STEM, ModGenes.photosynthesis, 0.7f),
//        )
//        allRecipes.addAll(geneFocusBrews)
//
//        val mutationBrews = listOf(
//            GmoRecipe(EntityType.ENDER_DRAGON, Items.ELYTRA, ModGenes.flight, 0.55f, isMutation = true),
//            GmoRecipe(EntityType.POLAR_BEAR, Items.NETHERITE_SWORD, ModGenes.strengthTwo, 0.5f, isMutation = true),
//            GmoRecipe(EntityType.SHULKER, Items.NETHERITE_CHESTPLATE, ModGenes.resistanceTwo, 0.5f, isMutation = true),
//            GmoRecipe(EntityType.POLAR_BEAR, Items.DIAMOND_SWORD, ModGenes.clawsTwo, 0.75f, isMutation = true),
//            GmoRecipe(EntityType.RABBIT, Items.DIAMOND_BOOTS, ModGenes.speedTwo, 0.5f, isMutation = true),
//            GmoRecipe(EntityType.OCELOT, Items.NETHERITE_BOOTS, ModGenes.speedFour, 0.5f, isMutation = true),
//            GmoRecipe(EntityType.RABBIT, Items.NETHERITE_PICKAXE, ModGenes.hasteTwo, 0.35f, isMutation = true),
//            GmoRecipe(
//                EntityType.SILVERFISH,
//                Items.NETHERITE_PICKAXE,
//                ModGenes.efficiencyFour,
//                0.25f,
//                isMutation = true
//            ),
//            GmoRecipe(EntityType.ZOMBIE, Items.FERMENTED_SPIDER_EYE, ModGenes.scareZombies, 0.5f, isMutation = true),
//            GmoRecipe(EntityType.SPIDER, Items.FERMENTED_SPIDER_EYE, ModGenes.scareSpiders, 0.5f, isMutation = true),
//            GmoRecipe(
//                EntityType.ENDER_DRAGON,
//                Items.ENCHANTED_GOLDEN_APPLE,
//                ModGenes.regenerationFour,
//                0.35f,
//                isMutation = true
//            ),
//            GmoRecipe(EntityType.PIG, Items.BLAZE_POWDER, ModGenes.meatyTwo, 0.75f, isMutation = true),
//            GmoRecipe(EntityType.ENDERMAN, Items.GOLDEN_APPLE, ModGenes.moreHeartsTwo, 0.25f, true)
//        )
//        allRecipes.addAll(mutationBrews)
//
//        val virusBrews = listOf(
//            VirusRecipe(ModGenes.poisonImmunity, ModGenes.poison),
//            VirusRecipe(ModGenes.witherHit, ModGenes.poisonFour),
//            VirusRecipe(ModGenes.witherProof, ModGenes.wither),
//            VirusRecipe(ModGenes.strength, ModGenes.weakness),
//            VirusRecipe(ModGenes.nightVision, ModGenes.blindness),
//            VirusRecipe(ModGenes.speed, ModGenes.slowness),
//            VirusRecipe(ModGenes.speedTwo, ModGenes.slownessFour),
//            VirusRecipe(ModGenes.speedFour, ModGenes.slownessSix),
//            VirusRecipe(ModGenes.milky, ModGenes.nausea),
//            VirusRecipe(ModGenes.meaty, ModGenes.nausea),
//            VirusRecipe(ModGenes.layEgg, ModGenes.nausea),
//            VirusRecipe(ModGenes.noHunger, ModGenes.hunger),
//            VirusRecipe(ModGenes.fireProof, ModGenes.flambe),
//            VirusRecipe(ModGenes.luck, ModGenes.cursed),
//            VirusRecipe(ModGenes.haste, ModGenes.miningFatigue),
//            VirusRecipe(ModGenes.scareCreepers, ModGenes.greenDeath),
//            VirusRecipe(ModGenes.scareSkeletons, ModGenes.unUndeath),
//            VirusRecipe(ModGenes.scareZombies, ModGenes.unUndeath),
//            VirusRecipe(ModGenes.resistance, ModGenes.grayDeath),
//            VirusRecipe(ModGenes.dragonsBreath, ModGenes.whiteDeath),
//
//            BlackDeathRecipe(),
//
//            BrewingRecipe(
//                viralAgentsPotionStack.`asIngredient()`,
//                ModItems.DNA_HELIX.itemStack.setGene(ModGenes.emeraldHeart).ingredient,
//                potionStack(ZOMBIFY_VILLAGER)
//            )
//        )
//        allRecipes.addAll(virusBrews)

        for (recipe in allRecipes) {
            event.builder.addRecipe(recipe)
        }

    }

}