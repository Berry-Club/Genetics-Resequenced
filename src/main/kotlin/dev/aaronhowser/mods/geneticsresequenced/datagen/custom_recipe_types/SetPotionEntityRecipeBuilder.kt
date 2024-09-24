package dev.aaronhowser.mods.geneticsresequenced.datagen.custom_recipe_types

import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.high_temp.SetPotionEntityRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

class SetPotionEntityRecipeBuilder : RecipeBuilder {
    private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()

    override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
        criteria[name] = criterion
        return this
    }

    override fun group(p0: String?): RecipeBuilder {
        error("Unsupported")
    }

    override fun getResult(): Item {
        return Items.POTION
    }

    override fun save(output: RecipeOutput, defaultId: ResourceLocation) {
        val id = OtherUtil.modResource("set_potion_entity")

        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)

        val recipe = SetPotionEntityRecipe()

        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
    }
}