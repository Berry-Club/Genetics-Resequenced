package dev.aaronhowser.mods.genetics_resequenced.recipe.incubator

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.genetics_resequenced.item.EntityDnaItem
import dev.aaronhowser.mods.genetics_resequenced.recipe.base.IncubatorRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.registry.ModRecipeSerializers
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class SetPotionEntityRecipe(
	bottomIngredient: Ingredient
) : IncubatorRecipe(
	topIngredient = Ingredient.of(ModItems.CELL),
	bottomIngredient = bottomIngredient
) {

	override fun matches(input: Input, level: Level): Boolean {
		val cellStack = input.getTopItem()
		val potionStack = input.getBottomItem()

		if (!this.topIngredient.test(cellStack)) return false
		if (!this.bottomIngredient.test(potionStack)) return false

		val topEntity = EntityDnaItem.getEntityType(cellStack) ?: return false
		val bottomEntity = EntityDnaItem.getEntityType(potionStack) ?: return true

		return topEntity != bottomEntity
	}

	override fun assemble(input: Input): ItemStack {
		val topItem = input.getTopItem()
		val bottomItem = input.getBottomItem()

		val topEntity = EntityDnaItem.getEntityType(topItem) ?: return ItemStack.EMPTY

		val output = bottomItem.copy()
		EntityDnaItem.setEntityType(output, topEntity)
		return output
	}

	fun getResultItem(): ItemStack {
		return ItemStack.EMPTY
	}

	override fun getSerializer(): RecipeSerializer<SetPotionEntityRecipe> {
		return ModRecipeSerializers.SET_POTION_ENTITY.get()
	}

	companion object {
		val CODEC: MapCodec<SetPotionEntityRecipe> =
			RecordCodecBuilder.mapCodec { instance ->
				instance.group(
					Ingredient.CODEC
						.fieldOf("bottom_slot")
						.forGetter(SetPotionEntityRecipe::bottomIngredient)
				).apply(instance, ::SetPotionEntityRecipe)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetPotionEntityRecipe> =
			StreamCodec.composite(
				Ingredient.CONTENTS_STREAM_CODEC, SetPotionEntityRecipe::bottomIngredient,
				::SetPotionEntityRecipe
			)
	}

}
