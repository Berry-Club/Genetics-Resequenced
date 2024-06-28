package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.getPotionContents
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.setPotion
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.brewing.BrewingRecipe
import net.neoforged.neoforge.common.brewing.IBrewingRecipe
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import net.neoforged.neoforge.registries.DeferredHolder
import kotlin.jvm.optionals.getOrNull

object BrewingRecipes {

    private val modPotions: List<Potion> by lazy {
        ModPotions.POTION_REGISTRY.entries.map { it.get() }
    }


    fun tooltip(event: ItemTooltipEvent) {
        val stack = event.itemStack
        val potionContents = stack.getPotionContents() ?: return
        val itemPotion = potionContents.potion.getOrNull()?.value() ?: return

        if (itemPotion == ModPotions.ZOMBIFY_VILLAGER || itemPotion == ModPotions.PANACEA) return
        if (itemPotion !in modPotions) return

        if (stack.item != Items.POTION) {
            event.toolTip.add(
                ModLanguageProvider.Tooltips.IGNORE_POTION
                    .toComponent()
                    .withStyle { it.withColor(ChatFormatting.RED) }
            )
        }

        val itemGene = DnaHelixItem.getGene(stack)
        val itemEntity = EntityDnaItem.getEntityType(stack)

        if (itemGene != null) {
            event.toolTip.add(
                ModLanguageProvider.Tooltips.GENE
                    .toComponent(itemGene.nameComponent)
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
        }

        if (itemEntity != null) {
            event.toolTip.add(
                ModLanguageProvider.Tooltips.HELIX_ENTITY
                    .toComponent(itemEntity.description)
                    .withStyle { it.withColor(ChatFormatting.GRAY) }
            )
        }

    }

    private fun Potion.asIngredient(): Ingredient {
        val stack = potionStack(this).setPotion(this@asIngredient)

        return Ingredient.of(stack)
    }

    private fun Holder<Potion>.asIngredient(): Ingredient {
        val stack = potionStack(this).setPotion(this@asIngredient)

        return Ingredient.of(stack)
    }

    private fun ItemStack.asIngredient(): Ingredient = Ingredient.of(this)

    private fun Item.asIngredient(): Ingredient = Ingredient.of(this.itemStack)

    val allRecipes: MutableList<IBrewingRecipe> = mutableListOf()

    fun potionStack(potion: Potion): ItemStack {
        return ItemStack(Items.POTION).setPotion(potion)
    }

    fun potionStack(potion: DeferredHolder<Potion, Potion>): ItemStack {
        return potionStack(potion.get())
    }

    fun potionStack(potion: Holder<Potion>): ItemStack {
        return potionStack(potion.value())
    }

    val substratePotionStack by lazy { potionStack(ModPotions.SUBSTRATE) }
    val cellGrowthPotionStack by lazy { potionStack(ModPotions.CELL_GROWTH) }
    val mutationPotionStack by lazy { potionStack(ModPotions.MUTATION) }
    val viralAgentsPotionStack by lazy { potionStack(ModPotions.VIRAL_AGENTS) }
    val curePotionStack by lazy { potionStack(ModPotions.PANACEA) }

    fun addRecipes() {

        val substrateRecipe = BrewingRecipe(
            Potions.MUNDANE.asIngredient(),
            ModItems.ORGANIC_MATTER.get().asIngredient(),
            substratePotionStack
        )
        val cellGrowthRecipe = BrewingRecipe(
            ModPotions.SUBSTRATE.asIngredient(),
            DnaHelixItem.setBasic(ModItems.DNA_HELIX.toStack()).asIngredient(),
            cellGrowthPotionStack
        )
        val mutationRecipe = BrewingRecipe(
            ModPotions.CELL_GROWTH.asIngredient(),
            Items.FERMENTED_SPIDER_EYE.asIngredient(),
            mutationPotionStack
        )
        val viralRecipe = BrewingRecipe(
            ModPotions.MUTATION.asIngredient(),
            Items.CHORUS_FRUIT.asIngredient(),
            viralAgentsPotionStack
        )
        val cureRecipe = BrewingRecipe(
            ModPotions.VIRAL_AGENTS.asIngredient(),
            DnaHelixItem.setGene(ModItems.DNA_HELIX.toStack(), ModGenes.emeraldHeart).asIngredient(),
            curePotionStack
        )

        allRecipes.addAll(
            listOf(
                substrateRecipe,
                cellGrowthRecipe,
                mutationRecipe,
                viralRecipe,
                cureRecipe
            )
        )
//
//        val substrateDuplicationRecipe = SubstrateCellRecipe()
//        allRecipes.add(substrateDuplicationRecipe)
//
//        val setPcgEntityRecipe = SetPotionEntityRecipe()
//        allRecipes.add(setPcgEntityRecipe)
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
//
//        for (recipe in allRecipes) {
//            BrewingRecipeRegistry.addRecipe(recipe)
//        }

    }

}