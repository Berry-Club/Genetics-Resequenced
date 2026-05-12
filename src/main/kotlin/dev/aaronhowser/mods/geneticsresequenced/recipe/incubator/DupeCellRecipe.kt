package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class DupeCellRecipe(
	val itemToDupe: Item,
	val amountToCreate: Int
) : IncubatorRecipe(
	topIngredient = Ingredient.of(itemToDupe),
	bottomIngredient = DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.SUBSTRATE)),
) {

	override fun matches(input: Input, level: Level): Boolean {
		val topStack = input.getTopItem()
		val potionStack = input.getBottomItem()

		if (!this.topIngredient.test(topStack)) return false
		if (!this.bottomIngredient.test(potionStack)) return false

		return EntityDnaItem.hasEntity(topStack)
	}

	override fun assemble(input: Input, lookup: HolderLookup.Provider): ItemStack {
		return input.getTopItem().copyWithCount(amountToCreate)
	}

	override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
		return itemToDupe.defaultInstance
	}

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.DUPE_CELL.get()
	}

	class Serializer : RecipeSerializer<DupeCellRecipe> {
		override fun codec(): MapCodec<DupeCellRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, DupeCellRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<DupeCellRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						BuiltInRegistries.ITEM
							.byNameCodec()
							.fieldOf("item_to_dupe")
							.forGetter(DupeCellRecipe::itemToDupe),
						Codec.INT
							.fieldOf("amount_to_create")
							.forGetter(DupeCellRecipe::amountToCreate)
					).apply(instance, ::DupeCellRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, DupeCellRecipe> =
				StreamCodec.composite(
					ByteBufCodecs.registry(Registries.ITEM), DupeCellRecipe::itemToDupe,
					ByteBufCodecs.INT, DupeCellRecipe::amountToCreate,
					::DupeCellRecipe
				)
		}
	}

}
