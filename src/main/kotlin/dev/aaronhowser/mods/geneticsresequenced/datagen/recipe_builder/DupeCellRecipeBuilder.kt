package dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder

import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.DupeCellRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item

class DupeCellRecipeBuilder(
    val isGmoCell: Boolean = false
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
        return ModItems.CELL.get()
    }

    override fun save(output: RecipeOutput, defaultId: ResourceLocation) {

        val idString = if (isGmoCell) "incubator/dupe_substrate_cell_gmo" else "incubator/dupe_substrate_cell"
        val id = OtherUtil.modResource(idString)

        val advancement = output.advancement()
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
            .rewards(AdvancementRewards.Builder.recipe(id))
            .requirements(AdvancementRequirements.Strategy.OR)

        criteria.forEach { (name, criterion) -> advancement.addCriterion(name, criterion) }

        val recipe = DupeCellRecipe(isGmoCell)

        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
    }
}