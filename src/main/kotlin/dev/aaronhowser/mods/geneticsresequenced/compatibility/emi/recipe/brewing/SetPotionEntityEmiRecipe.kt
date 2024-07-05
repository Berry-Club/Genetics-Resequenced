package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.crafting.Ingredient

class SetPotionEntityEmiRecipe(
    val entityType: EntityType<*>,
    val isMutation: Boolean
) : AbstractEmiBrewingRecipe() {

    companion object {
        fun getAllRecipes(): List<SetPotionEntityEmiRecipe> {
            val allEntityTypes = EntityDnaItem.validEntityTypes

            val pcgRecipes: MutableList<SetPotionEntityEmiRecipe> = mutableListOf()
            val mutationRecipes: MutableList<SetPotionEntityEmiRecipe> = mutableListOf()

            allEntityTypes.forEach {
                pcgRecipes.add(SetPotionEntityEmiRecipe(it, false))
                mutationRecipes.add(SetPotionEntityEmiRecipe(it, true))
            }

            return pcgRecipes + mutationRecipes
        }
    }

    override val ingredient: EmiIngredient
    override val input: EmiIngredient
    override val output: EmiStack

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.SET_ENTITY_CATEGORY
    }

    init {
        val cellStack = ModItems.CELL.toStack()
        EntityDnaItem.setEntityType(cellStack, entityType)
        ingredient = EmiIngredient.of(Ingredient.of(cellStack))

        val potionStack = if (isMutation) {
            BrewingRecipes.mutationPotionStack
        } else {
            BrewingRecipes.cellGrowthPotionStack
        }

        input = EmiIngredient.of(Ingredient.of(potionStack))

        EntityDnaItem.setEntityType(potionStack, entityType)
        output = EmiStack.of(potionStack)
    }

    override fun getId(): ResourceLocation {
        val potionString = if (isMutation) "mutation" else "pcg"
        val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')

        return OtherUtil.modResource("/set_potion_entity/$potionString/$entityTypeString")
    }

}