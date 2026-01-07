package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.core.RegistryAccess
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class SetPotionEntityRecipe(
	private val id: ResourceLocation,
	bottomIngredient: Ingredient
) : AbstractIncubatorRecipe(
	topIngredient = Ingredient.of(ModItems.CELL.get()),
	bottomIngredient = bottomIngredient
) {

	override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
		val cellStack = input.getTopItem()
		val potionStack = input.getBottomItem()

		if (!this.topIngredient.test(cellStack)) return false
		if (!this.bottomIngredient.test(potionStack)) return false

		val topEntity = EntityDnaItem.getEntityType(cellStack) ?: return false
		val bottomEntity = EntityDnaItem.getEntityType(potionStack) ?: return true

		return topEntity != bottomEntity
	}

	override fun assemble(input: IncubatorRecipeInput, pRegistryAccess: RegistryAccess): ItemStack {
		val topItem = input.getTopItem()
		val bottomItem = input.getBottomItem()

		val topEntity = EntityDnaItem.getEntityType(topItem) ?: return ItemStack.EMPTY

		val output = bottomItem.copy()
		EntityDnaItem.setEntityType(output, topEntity)
		return output
	}

	override fun getResultItem(pRegistryAccess: RegistryAccess): ItemStack {
		return ItemStack.EMPTY
	}

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.SET_POTION_ENTITY.get()
	}

	override fun getId(): ResourceLocation = id

	class Serializer : RecipeSerializer<SetPotionEntityRecipe> {

		override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): SetPotionEntityRecipe {
			val bottomIngredient = Ingredient.fromJson(pSerializedRecipe.get("bottom_ingredient"))
			return SetPotionEntityRecipe(pRecipeId, bottomIngredient)
		}

		override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): SetPotionEntityRecipe {
			val bottomIngredient = Ingredient.fromNetwork(pBuffer)
			return SetPotionEntityRecipe(pRecipeId, bottomIngredient)
		}

		override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: SetPotionEntityRecipe) {
			pRecipe.bottomIngredient.toNetwork(pBuffer)
		}

	}

}