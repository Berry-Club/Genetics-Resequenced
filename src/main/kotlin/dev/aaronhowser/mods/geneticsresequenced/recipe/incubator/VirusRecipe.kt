package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class VirusRecipe(
	val inputDnaGene: ResourceKey<Gene>,
	val outputGene: ResourceKey<Gene>
) : IncubatorRecipe(
	topIngredient = Ingredient.of(ModItems.DNA_HELIX.get()),
	bottomIngredient = OtherUtil.potionIngredient(ModPotions.VIRAL_AGENTS)
) {

	override fun matches(input: Input, level: Level): Boolean {
		val helixStack = input.getTopItem()
		val potionStack = input.getBottomItem()

		if (!this.topIngredient.test(helixStack)) return false
		if (!this.bottomIngredient.test(potionStack)) return false

		return DnaHelixItem.getGeneHolder(helixStack).isGene(inputDnaGene)
	}

	override fun assemble(input: Input): ItemStack {
		val inputGeneHolder = DnaHelixItem.getGeneHolder(input.getTopItem()) ?: return ItemStack.EMPTY
		val lookup = inputGeneHolder.unwrapLookup() ?: return ItemStack.EMPTY

		return createResult(lookup.getOrThrow(outputGene))
	}

	fun getResultItem(lookup: net.minecraft.core.HolderLookup.Provider): ItemStack {
		return createResult(this.outputGene.getHolderOrThrow(lookup))
	}

	private fun createResult(outputGeneHolder: net.minecraft.core.Holder<Gene>): ItemStack {
		return DnaHelixItem.setGeneHolder(ModItems.DNA_HELIX.toStack(), outputGeneHolder)
	}

	override fun getSerializer(): RecipeSerializer<VirusRecipe> {
		return ModRecipeSerializers.VIRUS.get()
	}


	companion object {
		@Suppress("UNCHECKED_CAST")
		fun getVirusRecipes(recipeManager: RecipeManager): List<RecipeHolder<VirusRecipe>> {
			val incubatorRecipes = getIncubatorRecipes(recipeManager)

//			return incubatorRecipes.mapNotNull { if (it.value is VirusRecipe) it as? RecipeHolder<VirusRecipe> else null }
			return buildList {
				for (recipe in incubatorRecipes) {
					if (recipe.value is VirusRecipe) {
						add(recipe as RecipeHolder<VirusRecipe>)
					}
				}
			}
		}

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
