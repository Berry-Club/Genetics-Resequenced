package dev.aaronhowser.mods.genetics_resequenced.datagen.recipe.builder

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.BasicIncubatorRecipe
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient

class BasicIncubatorRecipeBuilder(
	val topSlotIngredient: Ingredient,
	val bottomSlotIngredient: Ingredient,
	val outputTemplate: ItemStackTemplate,
	val recipeName: String? = null
) : RecipeBuilder {

	constructor(
		topSlotIngredient: Ingredient,
		bottomSlotIngredient: Ingredient,
		outputStack: ItemStack,
		recipeName: String? = null
	) : this(
		topSlotIngredient,
		bottomSlotIngredient,
		ItemStackTemplate.fromNonEmptyStack(outputStack),
		recipeName
	)

	private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
		criteria[name] = criterion
		return this
	}

	override fun group(p0: String?): RecipeBuilder {
		error("Unsupported")
	}

	override fun defaultId(): ResourceKey<Recipe<*>> =
		recipeKey(outputTemplate.item().key!!.identifier().path)

	override fun save(output: RecipeOutput, defaultId: ResourceKey<Recipe<*>>) {
		val idString = StringBuilder()

		idString
			.append("incubator/basic/")
			.append(recipeName ?: defaultId.identifier().path)

		val id = recipeKey(idString.toString())

		val advancement = output.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR)

		criteria.forEach { (name, criterion) -> advancement.addCriterion(name, criterion) }

		val recipe = BasicIncubatorRecipe(
			topSlotIngredient,
			bottomSlotIngredient,
			outputTemplate,
			isLowTemp = false
		)

		output.accept(id, recipe, advancement.build(id.identifier().withPrefix("recipes/")))
	}

	private fun recipeKey(path: String): ResourceKey<Recipe<*>> =
		ResourceKey.create(Registries.RECIPE, GeneticsResequenced.modResource(path))
}
