package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.core.RegistryAccess
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.minecraftforge.common.crafting.CraftingHelper

class BasicIncubatorRecipe(
	private val id: ResourceLocation,
	topIngredient: Ingredient,
	bottomIngredient: Ingredient,
	val outputStack: ItemStack,
	val isLowTemp: Boolean
) : AbstractIncubatorRecipe(topIngredient, bottomIngredient) {

	override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
		val topItem = input.getTopItem()
		val bottomItem = input.getBottomItem()

		return this.isLowTemp == input.isLowTemp
				&& this.topIngredient.test(topItem)
				&& this.bottomIngredient.test(bottomItem)
	}

	override fun assemble(input: IncubatorRecipeInput, pRegistryAccess: RegistryAccess): ItemStack {
		return getResultItem(pRegistryAccess)
	}

	override fun getResultItem(pRegistryAccess: RegistryAccess): ItemStack {
		return this.outputStack.copy()
	}

	override fun getId(): ResourceLocation = this.id

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.BASIC_INCUBATOR.get()
	}

	class Serializer : RecipeSerializer<BasicIncubatorRecipe> {
		override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): BasicIncubatorRecipe {
			val topIngredient = Ingredient.fromJson(pSerializedRecipe.getAsJsonObject("top_ingredient"))
			val bottomIngredient = Ingredient.fromJson(pSerializedRecipe.getAsJsonObject("bottom_ingredient"))
			val outputStack = CraftingHelper.getItemStack(pSerializedRecipe.getAsJsonObject("output"), true)
			val isLowTemp = GsonHelper.getAsBoolean(pSerializedRecipe, "is_low_temp", false)

			return BasicIncubatorRecipe(
				id = pRecipeId,
				topIngredient = topIngredient,
				bottomIngredient = bottomIngredient,
				outputStack = outputStack,
				isLowTemp = isLowTemp
			)
		}

		override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): BasicIncubatorRecipe {
			val topIngredient = Ingredient.fromNetwork(pBuffer)
			val bottomIngredient = Ingredient.fromNetwork(pBuffer)
			val outputStack = pBuffer.readItem()
			val isLowTemp = pBuffer.readBoolean()

			return BasicIncubatorRecipe(
				id = pRecipeId,
				topIngredient = topIngredient,
				bottomIngredient = bottomIngredient,
				outputStack = outputStack,
				isLowTemp = isLowTemp
			)
		}

		override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: BasicIncubatorRecipe) {
			pRecipe.topIngredient.toNetwork(pBuffer)
			pRecipe.bottomIngredient.toNetwork(pBuffer)
			pBuffer.writeItem(pRecipe.outputStack)
			pBuffer.writeBoolean(pRecipe.isLowTemp)
		}
	}

	companion object {
		fun getBasicRecipes(recipeManager: RecipeManager): List<BasicIncubatorRecipe> {
			return getIncubatorRecipes(recipeManager).filterIsInstance<BasicIncubatorRecipe>()
		}
	}

}