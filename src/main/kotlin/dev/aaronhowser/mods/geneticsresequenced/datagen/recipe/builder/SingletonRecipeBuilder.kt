package dev.aaronhowser.mods.geneticsresequenced.datagen.recipe.builder

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe

class SingletonRecipeBuilder(
	val recipe: Recipe<*>,
	val resultItem: Item,
	val idString: String
) : RecipeBuilder {
	private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
		criteria[name] = criterion
		return this
	}

	override fun group(p0: String?): RecipeBuilder {
		error("Unsupported")
	}

	override fun defaultId(): ResourceKey<Recipe<*>> =
		recipeKey(idString)

	override fun save(output: RecipeOutput, defaultId: ResourceKey<Recipe<*>>) {
		val id = defaultId

		val advancement = output.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR)

		criteria.forEach { (name, criterion) -> advancement.addCriterion(name, criterion) }

		output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/")))
	}

	private fun recipeKey(path: String): ResourceKey<Recipe<*>> =
		ResourceKey.create(Registries.RECIPE, GeneticsResequenced.modResource(path))
}
