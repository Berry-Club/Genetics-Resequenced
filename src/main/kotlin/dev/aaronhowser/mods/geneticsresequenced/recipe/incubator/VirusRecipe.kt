package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.google.gson.JsonObject
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.AaronExtensions.partialNbtIngredient
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
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

		return DnaHelixItem.getGeneHolder(helixStack).isGene(inputDnaGene)
	}

	override fun assemble(pContainer: IncubatorRecipeInput, pRegistryAccess: RegistryAccess): ItemStack {
		return getResultItem(pRegistryAccess)
	}

	override fun getResultItem(pRegistryAccess: RegistryAccess): ItemStack {
		val output = DnaHelixItem.setGeneHolder(
			ModItems.DNA_HELIX.getDefaultInstance(),
			this.outputGene.getHolderOrThrow(pRegistryAccess)
		)

		return output
	}

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.VIRUS.get()
	}

	class Serializer : RecipeSerializer<VirusRecipe> {

		override fun codec(): MapCodec<VirusRecipe> {
			return CODEC
		}

		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, VirusRecipe> {
			return STREAM_CODEC
		}

		override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): VirusRecipe {
			TODO("Not yet implemented")
		}

		override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): VirusRecipe? {
			TODO("Not yet implemented")
		}

		override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: VirusRecipe) {
			TODO("Not yet implemented")
		}

		companion object {
			val CODEC: MapCodec<VirusRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
							.fieldOf("input_gene")
							.forGetter(VirusRecipe::inputDnaGene),
						ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
							.fieldOf("output_gene")
							.forGetter(VirusRecipe::outputGene)
					).apply(instance, ::VirusRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, VirusRecipe> =
				StreamCodec.composite(
					ResourceKey.streamCodec(ModGenes.GENE_REGISTRY_KEY), VirusRecipe::inputDnaGene,
					ResourceKey.streamCodec(ModGenes.GENE_REGISTRY_KEY), VirusRecipe::outputGene,
					::VirusRecipe
				)

		}

	}

	companion object {
		@Suppress("UNCHECKED_CAST")
		fun getVirusRecipes(recipeManager: RecipeManager): List<RecipeHolder<VirusRecipe>> {
			val incubatorRecipes = getIncubatorRecipes(recipeManager)

			return incubatorRecipes.mapNotNull { if (it.value is VirusRecipe) it as? RecipeHolder<VirusRecipe> else null }
		}
	}

}