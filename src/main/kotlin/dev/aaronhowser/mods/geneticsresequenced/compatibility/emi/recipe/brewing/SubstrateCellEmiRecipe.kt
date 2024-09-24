package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.high_temp.GmoRecipe
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class SubstrateCellEmiRecipe(
    val cellStack: ItemStack
) : AbstractEmiBrewingRecipe() {

    companion object {
        fun getAllRecipes(recipeManager: RecipeManager): List<SubstrateCellEmiRecipe> {
            val recipes = mutableListOf<SubstrateCellEmiRecipe>()

            val allEntityTypes = EntityDnaItem.validEntityTypes
            for (entityType in allEntityTypes) {
                val cellStack = ModItems.CELL.toStack()
                EntityDnaItem.setEntityType(cellStack, entityType)

                recipes.add(SubstrateCellEmiRecipe(cellStack))
            }

            val allGmoRecipes = GmoRecipe.getGmoRecipes(recipeManager)
            for (recipe in allGmoRecipes) {
                val entityType = recipe.value.entityType
                val goodGene = recipe.value.idealGeneRk

                val gmoCellStack = ModItems.GMO_CELL.toStack()
                GmoCell.setDetails(
                    gmoCellStack,
                    entityType,
                    goodGene
                )

                recipes.add(SubstrateCellEmiRecipe(gmoCellStack))
            }

            return recipes
        }
    }

    override val ingredient: EmiIngredient = EmiIngredient.of(Ingredient.of(cellStack))
    override val input: EmiIngredient = EmiIngredient.of(Ingredient.of(BrewingRecipes.substratePotionStack))
    override val output: EmiStack = EmiStack.of(cellStack.copy())

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.CELL_DUPE_CATEGORY
    }

    override val tooltips: List<Component> = listOf(
        ModLanguageProvider.Recipe.SUBSTRATE.toComponent().withColor(ChatFormatting.GRAY)
    )

    override fun getId(): ResourceLocation {
        var string = "/substrate_dupe/"

        val entityType = EntityDnaItem.getEntityType(cellStack) ?: error("Cell stack has no entity type!")
        val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')

        string += entityTypeString

        if (cellStack.item == ModItems.GMO_CELL.get()) {
            val geneHolder = DnaHelixItem.getGeneHolder(cellStack)
                ?: error("GMO Cell stack has no gene!")
            val geneString = geneHolder.key!!.location().toString().replace(':', '/')

            string += "/$geneString"
        }

        return OtherUtil.modResource(string)
    }

}