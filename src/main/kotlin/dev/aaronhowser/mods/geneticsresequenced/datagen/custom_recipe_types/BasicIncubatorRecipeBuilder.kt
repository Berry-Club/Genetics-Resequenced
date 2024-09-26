package dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types

import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.BasicIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class BasicIncubatorRecipeBuilder(
    val topSlotIngredient: Ingredient,
    val bottomSlotIngredient: Ingredient,
    val outputStack: ItemStack,
    val recipeName: String? = null
) : RecipeBuilder {

    private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
        criteria[name] = criterion
        return this
    }

    override fun group(p0: String?): RecipeBuilder {
        error("Unsupported")
    }

    override fun getResult(): Item {
        return outputStack.item
    }

    override fun save(output: RecipeOutput, defaultId: ResourceLocation) {
        val idString = StringBuilder()

        idString
            .append("incubator/basic/")
            .append(recipeName ?: defaultId.path)

        val id = OtherUtil.modResource(idString.toString())

        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)

        criteria.forEach { (name, criterion) -> advancement.addCriterion(name, criterion) }

        val recipe = BasicIncubatorRecipe(topSlotIngredient, bottomSlotIngredient, outputStack)

        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
    }
}