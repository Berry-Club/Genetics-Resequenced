package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.crafting.Ingredient

class SubstrateCellEmiRecipe(
    val entityType: EntityType<*>
) : AbstractEmiBrewingRecipe() {

    companion object {
        fun getAllRecipes(): List<SubstrateCellEmiRecipe> {
            val allEntityTypes = EntityDnaItem.validEntityTypes

            return allEntityTypes.map { SubstrateCellEmiRecipe(it) }
        }
    }

    override val ingredient: EmiIngredient
    override val input: EmiIngredient = EmiIngredient.of(Ingredient.of(BrewingRecipes.substratePotionStack))
    override val output: EmiStack

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.CELL_DUPE_CATEGORY
    }

    init {
        val cellStack = ModItems.CELL.toStack()
        EntityDnaItem.setEntityType(cellStack, entityType)
        ingredient = EmiIngredient.of(Ingredient.of(cellStack))

        output = EmiStack.of(cellStack)
    }

    override val tooltips: List<Component> = listOf(
        ModLanguageProvider.Recipe.SUBSTRATE.toComponent().withColor(ChatFormatting.GRAY)
    )

    override fun getId(): ResourceLocation {
        val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')

        return OtherUtil.modResource("/substrate_dupe/$entityTypeString")
    }

}