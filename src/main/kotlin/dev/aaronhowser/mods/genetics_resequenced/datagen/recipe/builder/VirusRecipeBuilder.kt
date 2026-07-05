package dev.aaronhowser.mods.genetics_resequenced.datagen.recipe.builder

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.VirusRecipe
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe

class VirusRecipeBuilder(
	val inputDnaGene: ResourceKey<Gene>,
	val outputGene: ResourceKey<Gene>
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
		recipeKey(recipePath())

	override fun save(output: RecipeOutput, defaultId: ResourceKey<Recipe<*>>) {
		val id = recipeKey(recipePath())

		val advancement = output.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR)

		criteria.forEach { (name, criterion) -> advancement.addCriterion(name, criterion) }

		val recipe = VirusRecipe(inputDnaGene, outputGene)

		output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/")))
	}

	private fun recipePath(): String {
		val idString = StringBuilder()

		idString
			.append("incubator/")
			.append("virus/")
			.append(inputDnaGene.identifier().path)
			.append("_to_")
			.append(outputGene.identifier().path)

		return idString.toString()
	}

	private fun recipeKey(path: String): ResourceKey<Recipe<*>> =
		ResourceKey.create(Registries.RECIPE, GeneticsResequenced.modId(path))
}
