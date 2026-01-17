package dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.function.Consumer

class BasicIncubatorRecipeBuilder(
	val topSlotIngredient: Ingredient,
	val bottomSlotIngredient: Ingredient,
	val outputStack: ItemStack,
	val baseName: String
) : RecipeBuilder {

	fun getName(): String {
		val pathBuilder = StringBuilder("incubator/")
			.append(baseName)

		return GeneticsResequenced.modResource(pathBuilder.toString()).toString()
	}

	private val advancement: Advancement.Builder = Advancement.Builder.recipeAdvancement()

	override fun unlockedBy(pCriterionName: String, pCriterionTrigger: CriterionTriggerInstance): RecipeBuilder {
		advancement.addCriterion(pCriterionName, pCriterionTrigger)
		return this
	}

	override fun group(pGroupName: String?): RecipeBuilder {
		error("No group >:(")
	}

	override fun getResult(): Item = outputStack.item

	override fun save(pFinishedRecipeConsumer: Consumer<FinishedRecipe>, pRecipeId: ResourceLocation) {
		pFinishedRecipeConsumer.accept(
			Result(
				pRecipeId,
				outputStack,
				topSlotIngredient,
				bottomSlotIngredient,
				advancement,
				pRecipeId.withPrefix("recipes/incubator/")
			)
		)
	}

	class Result(
		val recipeId: ResourceLocation,
		val result: ItemStack,
		val topSlotIngredient: Ingredient,
		val bottomSlotIngredient: Ingredient,
		val advancementBuilder: Advancement.Builder,
		private val advancementId: ResourceLocation
	) : FinishedRecipe {

		override fun serializeRecipeData(pJson: JsonObject) {
			pJson.add("top_slot", topSlotIngredient.toJson())
			pJson.add("bottom_slot", bottomSlotIngredient.toJson())

			val resultJson = JsonObject()
			resultJson.addProperty("item", BuiltInRegistries.ITEM.getKey(result.item).toString())

			if (result.count > 1) {
				resultJson.addProperty("count", result.count)
			}

			val tag = result.tag
			if (tag != null) {
				resultJson.addProperty("nbt", tag.toString())
			}

			pJson.add("result", resultJson)
		}

		override fun getId(): ResourceLocation = this.recipeId
		override fun getType(): RecipeSerializer<*> = ModRecipeSerializers.BASIC_INCUBATOR.get()
		override fun serializeAdvancement(): JsonObject = advancementBuilder.serializeToJson()
		override fun getAdvancementId(): ResourceLocation = advancementId

	}

}