package dev.aaronhowser.mods.geneticsresequenced.datagen.recipe_builder

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.CriterionTriggerInstance
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.function.Consumer

class VirusRecipeBuilder(
	val inputDnaGene: ResourceKey<Gene>,
	val outputGene: ResourceKey<Gene>
) : RecipeBuilder {

	private val advancement: Advancement.Builder = Advancement.Builder.recipeAdvancement()

	override fun unlockedBy(pCriterionName: String, pCriterionTrigger: CriterionTriggerInstance): RecipeBuilder {
		advancement.addCriterion(pCriterionName, pCriterionTrigger)
		return this
	}

	override fun group(pGroupName: String?): RecipeBuilder {
		error("No group >:(")
	}

	override fun getResult(): Item = ModItems.DNA_HELIX.get()

	override fun save(pFinishedRecipeConsumer: Consumer<FinishedRecipe?>, pRecipeId: ResourceLocation) {
		pFinishedRecipeConsumer.accept(
			Result(
				pRecipeId,
				inputDnaGene,
				outputGene,
				advancement,
				pRecipeId.withPrefix("recipes/virus/")
			)
		)
	}

	class Result(
		val recipeId: ResourceLocation,
		val inputDnaGene: ResourceKey<Gene>,
		val outputGene: ResourceKey<Gene>,
		val advancement: Advancement.Builder,
		val advancementId: ResourceLocation
	) : FinishedRecipe {

		override fun serializeRecipeData(pJson: JsonObject) {
			pJson.addProperty("input_dna_gene", inputDnaGene.location().toString())
			pJson.addProperty("output_gene", outputGene.location().toString())
		}

		override fun getType(): RecipeSerializer<*> = ModRecipeSerializers.VIRUS.get()
		override fun getId(): ResourceLocation = recipeId
		override fun serializeAdvancement(): JsonObject = advancement.serializeToJson()
		override fun getAdvancementId(): ResourceLocation = advancementId
	}

}