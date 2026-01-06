package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.google.gson.JsonObject
import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModPotionTagsProvider
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

object SetPotionEntityRecipe : AbstractIncubatorRecipe(
	topIngredient = Ingredient.of(ModItems.CELL.get()),
	bottomIngredient = PotionTagIngredient(ModPotionTagsProvider.CAN_HAVE_ENTITY).toVanilla()
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

	class Serializer : RecipeSerializer<SetPotionEntityRecipe> {

		override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): SetPotionEntityRecipe {
			TODO("Not yet implemented")
		}

		override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): SetPotionEntityRecipe? {
			TODO("Not yet implemented")
		}

		override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: SetPotionEntityRecipe) {
			TODO("Not yet implemented")
		}

		companion object {
			val CODEC: MapCodec<SetPotionEntityRecipe> = MapCodec.unit(SetPotionEntityRecipe)
		}

	}

}