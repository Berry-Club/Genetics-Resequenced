package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class BasicIncubatorRecipe(
	topIngredient: Ingredient,
	bottomIngredient: Ingredient,
	private val outputTemplate: ItemStackTemplate,
	val isLowTemp: Boolean
) : IncubatorRecipe(topIngredient, bottomIngredient) {

	constructor(
		topIngredient: Ingredient,
		bottomIngredient: Ingredient,
		outputStack: ItemStack,
		isLowTemp: Boolean
	) : this(
		topIngredient,
		bottomIngredient,
		ItemStackTemplate.fromNonEmptyStack(outputStack),
		isLowTemp
	)

	val outputStack: ItemStack
		get() = outputTemplate.create()

	override fun matches(input: Input, level: Level): Boolean {
		val topItem = input.getTopItem()
		val bottomItem = input.getBottomItem()

		return this.isLowTemp == input.isLowTemp
				&& this.topIngredient.test(topItem)
				&& this.bottomIngredient.test(bottomItem)
	}

	override fun assemble(input: Input): ItemStack {
		return this.outputStack.copy()
	}

	fun getResultItem(): ItemStack {
		return this.outputStack.copy()
	}

	override fun getSerializer(): RecipeSerializer<BasicIncubatorRecipe> {
		return ModRecipeSerializers.BASIC_INCUBATOR.get()
	}

	companion object {
		@Suppress("UNCHECKED_CAST")
		fun getBasicRecipes(recipeManager: RecipeManager): List<RecipeHolder<BasicIncubatorRecipe>> {
			val incubatorRecipes = getIncubatorRecipes(recipeManager)

			return buildList {
				for (recipe in incubatorRecipes) {
					if (recipe.value is BasicIncubatorRecipe) {
						add(recipe as RecipeHolder<BasicIncubatorRecipe>)
					}
				}
			}
		}

		val CODEC: MapCodec<BasicIncubatorRecipe> =
			RecordCodecBuilder.mapCodec { instance ->
				instance.group(
					Ingredient.CODEC
						.fieldOf("top_slot")
						.forGetter(BasicIncubatorRecipe::topIngredient),
					Ingredient.CODEC
						.fieldOf("bottom_slot")
						.forGetter(BasicIncubatorRecipe::bottomIngredient),
					ItemStackTemplate.CODEC
						.fieldOf("output")
						.forGetter(BasicIncubatorRecipe::outputTemplate),
					Codec.BOOL
						.fieldOf("is_low_temperature")
						.forGetter(BasicIncubatorRecipe::isLowTemp)
				).apply(instance, ::BasicIncubatorRecipe)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BasicIncubatorRecipe> =
			StreamCodec.composite(
				Ingredient.CONTENTS_STREAM_CODEC, BasicIncubatorRecipe::topIngredient,
				Ingredient.CONTENTS_STREAM_CODEC, BasicIncubatorRecipe::bottomIngredient,
				ItemStackTemplate.STREAM_CODEC, BasicIncubatorRecipe::outputTemplate,
				ByteBufCodecs.BOOL, BasicIncubatorRecipe::isLowTemp,
				::BasicIncubatorRecipe
			)
	}

}
