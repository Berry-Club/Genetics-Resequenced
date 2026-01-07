package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.AaronExtensions.isTrue
import dev.aaronhowser.mods.aaron.AaronExtensions.partialNbtIngredient
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneDataComponent
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.RegistryAccess
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class VirusRecipe(
	private val id: ResourceLocation,
	val inputDnaGene: ResourceKey<Gene>,
	val outputGene: ResourceKey<Gene>
) : AbstractIncubatorRecipe(
	topIngredient = Ingredient.of(ModItems.DNA_HELIX.get()),
	bottomIngredient = OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS.get()).partialNbtIngredient()
) {

	override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
		val helixStack = input.getTopItem()
		val potionStack = input.getBottomItem()

		if (!this.topIngredient.test(helixStack)) return false
		if (!this.bottomIngredient.test(potionStack)) return false

		return GeneDataComponent.getGeneRk(helixStack)?.isGene(inputDnaGene).isTrue()
	}

	override fun assemble(pContainer: IncubatorRecipeInput, pRegistryAccess: RegistryAccess): ItemStack {
		return getResultItem(pRegistryAccess)
	}

	override fun getResultItem(pRegistryAccess: RegistryAccess): ItemStack {
		val output = GeneDataComponent.setGene(
			ModItems.DNA_HELIX.getDefaultInstance(),
			this.outputGene
		)

		return output
	}

	override fun getId(): ResourceLocation = this.id

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.VIRUS.get()
	}

	class Serializer : RecipeSerializer<VirusRecipe> {

		override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): VirusRecipe {
			val inputGeneId = ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
				.parse(JsonOps.INSTANCE, pSerializedRecipe.get("input_dna_gene"))
				.getOrThrow(false, ::IllegalArgumentException)
			val outputGeneId = ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
				.parse(JsonOps.INSTANCE, pSerializedRecipe.get("output_gene"))
				.getOrThrow(false, ::IllegalArgumentException)

			return VirusRecipe(
				id = pRecipeId,
				inputDnaGene = inputGeneId,
				outputGene = outputGeneId
			)
		}

		override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): VirusRecipe {
			val inputGeneId = pBuffer.readResourceKey(ModGenes.GENE_REGISTRY_KEY)
			val outputGeneId = pBuffer.readResourceKey(ModGenes.GENE_REGISTRY_KEY)

			return VirusRecipe(
				id = pRecipeId,
				inputDnaGene = inputGeneId,
				outputGene = outputGeneId
			)
		}

		override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: VirusRecipe) {
			pBuffer.writeResourceKey(pRecipe.inputDnaGene)
			pBuffer.writeResourceKey(pRecipe.outputGene)
		}

	}

	companion object {
		@Suppress("UNCHECKED_CAST")
		fun getVirusRecipes(recipeManager: RecipeManager): List<VirusRecipe> {
			return getIncubatorRecipes(recipeManager).filterIsInstance<VirusRecipe>()
		}
	}

}