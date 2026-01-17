package dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.function.Consumer

class DupeCellRecipeBuilder(
	val itemToDupe: Item,
	val amountToCreate: Int,
	val name: String
) : RecipeBuilder {

	private val advancement: Advancement.Builder = Advancement.Builder.recipeAdvancement()

	override fun unlockedBy(pCriterionName: String, pCriterionTrigger: CriterionTriggerInstance): RecipeBuilder {
		advancement.addCriterion(pCriterionName, pCriterionTrigger)
		return this
	}

	override fun group(pGroupName: String?): RecipeBuilder {
		error("No group >:(")
	}

	override fun getResult(): Item = itemToDupe

	override fun save(pFinishedRecipeConsumer: Consumer<FinishedRecipe>, pRecipeId: ResourceLocation) {
		pFinishedRecipeConsumer.accept(
			Result(
				pRecipeId,
				itemToDupe,
				amountToCreate,
				advancement,
				pRecipeId.withPrefix("recipes/dupe_cell/")
			)
		)
	}

	class Result(
		val recipeId: ResourceLocation,
		val itemToDupe: Item,
		val amountToCreate: Int,
		val advancementBuilder: Advancement.Builder,
		private val advancementId: ResourceLocation
	) : FinishedRecipe {

		override fun serializeRecipeData(pJson: JsonObject) {
			pJson.addProperty("item_to_dupe", BuiltInRegistries.ITEM.getKey(this.itemToDupe).toString())
			pJson.addProperty("amount_to_create", amountToCreate)
		}

		override fun getType(): RecipeSerializer<*> = ModRecipeSerializers.DUPE_CELL.get()
		override fun getId(): ResourceLocation = recipeId
		override fun serializeAdvancement(): JsonObject = advancementBuilder.serializeToJson()
		override fun getAdvancementId(): ResourceLocation = advancementId

	}

}