package dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder

import com.google.gson.JsonObject
import dev.aaronhowser.mods.aaron.AaronExtensions.getLocationOrNull
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.function.Consumer

class GmoRecipeBuilder(
	private val entityType: EntityType<*>,
	private val ingredient: Ingredient,
	private val idealGeneRk: ResourceKey<Gene>,
	private val geneChance: Float,
	private val needsMutationPotion: Boolean = false
) : RecipeBuilder {

	fun getName(): String {
		val entityString = EntityType.getKey(entityType).path
		val geneString = idealGeneRk.location().path

		val chanceString = if (geneChance == 1f) {
			"100"
		} else {
			(geneChance * 100).toInt().toString()
		}

		val pathBuilder = StringBuilder("incubator/gmo/")

		if (needsMutationPotion) {
			pathBuilder.append("mutation/")
		}

		pathBuilder
			.append(geneString)
			.append("_from_")
			.append(entityString)
			.append("_with_")
			.append(chanceString)
			.append("_chance")

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

	override fun getResult(): Item = ModItems.GMO_CELL.get()

	override fun save(pFinishedRecipeConsumer: Consumer<FinishedRecipe>, pRecipeId: ResourceLocation) {
		pFinishedRecipeConsumer.accept(
			Result(
				pRecipeId,
				entityType,
				ingredient,
				idealGeneRk,
				geneChance,
				needsMutationPotion,
				advancement,
				pRecipeId.withPrefix("recipes/gmo/")
			)
		)
	}

	class Result(
		val recipeId: ResourceLocation,
		val entityType: EntityType<*>,
		val ingredient: Ingredient,
		val idealGeneRk: ResourceKey<Gene>,
		val geneChance: Float,
		val needsMutationPotion: Boolean,
		val advancementBuilder: Advancement.Builder,
		private val advancementId: ResourceLocation
	) : FinishedRecipe {

		override fun serializeRecipeData(pJson: JsonObject) {
			pJson.addProperty("entity_type", entityType.builtInRegistryHolder().getLocationOrNull().toString())
			pJson.add("ingredient", ingredient.toJson())
			pJson.addProperty("ideal_gene", idealGeneRk.location().toString())
			pJson.addProperty("gene_chance", geneChance)
			pJson.addProperty("needs_mutation_potion", needsMutationPotion)
		}

		override fun getType(): RecipeSerializer<*> = ModRecipeSerializers.GMO.get()
		override fun getId(): ResourceLocation = recipeId
		override fun serializeAdvancement(): JsonObject = advancementBuilder.serializeToJson()
		override fun getAdvancementId(): ResourceLocation = advancementId
	}

}