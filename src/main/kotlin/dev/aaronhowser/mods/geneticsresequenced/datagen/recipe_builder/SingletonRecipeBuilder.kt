package dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder

import com.google.gson.JsonObject
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.function.Consumer

class SingletonRecipeBuilder(
	val recipe: Recipe<*>,
	val resultItem: Item
) : RecipeBuilder {

	private val advancement: Advancement.Builder = Advancement.Builder.recipeAdvancement()

	override fun unlockedBy(pCriterionName: String, pCriterionTrigger: CriterionTriggerInstance): RecipeBuilder {
		advancement.addCriterion(pCriterionName, pCriterionTrigger)
		return this
	}

	override fun group(pGroupName: String?): RecipeBuilder {
		error("No group >:(")
	}

	override fun getResult(): Item = resultItem

	override fun save(pFinishedRecipeConsumer: Consumer<FinishedRecipe?>, pRecipeId: ResourceLocation) {
		pFinishedRecipeConsumer.accept(
			Result(
				pRecipeId,
				recipe,
				advancement,
				pRecipeId
			)
		)
	}

	class Result(
		val recipeId: ResourceLocation,
		val recipe: Recipe<*>,
		val advancement: Advancement.Builder,
		val advancementId: ResourceLocation
	) : FinishedRecipe {

		override fun serializeRecipeData(pJson: JsonObject) {

		}

		override fun getType(): RecipeSerializer<*> = recipe.serializer
		override fun getId(): ResourceLocation = recipeId
		override fun serializeAdvancement(): JsonObject = advancement.serializeToJson()
		override fun getAdvancementId(): ResourceLocation = advancementId
	}
	)

}