package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.google.gson.JsonObject
import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.AaronExtensions.partialNbtIngredient
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneDataComponent
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.RegistryAccess
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class DupeCellRecipe(
	private val id: ResourceLocation,
	val isGmoCell: Boolean = false
) : AbstractIncubatorRecipe(
	topIngredient = Ingredient.of(if (isGmoCell) ModItems.GMO_CELL.get() else ModItems.CELL.get()),
	bottomIngredient = OtherUtil.getPotionStack(ModPotions.SUBSTRATE.get()).partialNbtIngredient(),
) {

	override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
		val topStack = input.getTopItem()
		val potionStack = input.getBottomItem()

		if (!this.topIngredient.test(topStack)) return false
		if (!this.bottomIngredient.test(potionStack)) return false

		return EntityDnaItem.hasEntity(topStack)
	}

	override fun assemble(input: IncubatorRecipeInput, pRegistryAccess: RegistryAccess): ItemStack {
		val topStack = input.getTopItem()

		val ingredientEntity = EntityDnaItem.getEntityType(topStack) ?: return ItemStack.EMPTY

		val outputCell: ItemStack

		if (this.isGmoCell) {
			val pIngredientGene = GeneDataComponent.getGeneRk(topStack) ?: return ItemStack.EMPTY

			outputCell = ModItems.GMO_CELL.getDefaultInstance()
			GmoCell.setDetails(outputCell, ingredientEntity, pIngredientGene)
		} else {
			outputCell = ModItems.CELL.getDefaultInstance()
			EntityDnaItem.setEntityType(outputCell, ingredientEntity)
		}

		return outputCell
	}

	override fun getResultItem(pRegistryAccess: RegistryAccess): ItemStack {
		return if (this.isGmoCell) ModItems.GMO_CELL.getDefaultInstance() else ModItems.CELL.getDefaultInstance()
	}

	override fun getId(): ResourceLocation = this.id

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.DUPE_CELL.get()
	}

	class Serializer : RecipeSerializer<DupeCellRecipe> {

		override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): DupeCellRecipe {
			val isGmoCell = GsonHelper.getAsBoolean(pSerializedRecipe, "is_gmo_cell", false)
			return DupeCellRecipe(pRecipeId, isGmoCell)
		}

		override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): DupeCellRecipe {
			val isGmoCell = pBuffer.readBoolean()
			return DupeCellRecipe(pRecipeId, isGmoCell)
		}

		override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: DupeCellRecipe) {
			pBuffer.writeBoolean(pRecipe.isGmoCell)
		}

	}

}
