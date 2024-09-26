package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.brewing.BrewingRecipe
import net.neoforged.neoforge.common.crafting.DataComponentIngredient
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

object BrewingRecipes {

    private val modPotions: List<Potion>
        get() = ModPotions.POTION_REGISTRY.entries.map { it.get() }

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

        val substrateRecipe = BrewingRecipe(
            ingredient(Potions.MUNDANE),
            ingredient(ModItems.ORGANIC_MATTER),
            substratePotionStack
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

        val allRecipes = listOf(
            substrateRecipe,
            mutationRecipe,
            viralRecipe,
        )

        for (recipe in allRecipes) {
            event.builder.addRecipe(recipe)
        }
    }

}