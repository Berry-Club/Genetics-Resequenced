package dev.aaronhowser.mods.genetics_resequenced.recipe.crafting

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.genetics_resequenced.item.PlasmidItem
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.registry.ModRecipeSerializers
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class UnsetAntiPlasmidRecipe : CustomRecipe() {

	override fun matches(input: CraftingInput, level: Level): Boolean {
		var antiPlasmid: ItemStack? = null

		for (stack in input.items()) {
			if (stack.item == ModItems.ANTI_PLASMID.get() && PlasmidItem.hasGene(stack)) {
				if (antiPlasmid != null) return false
				antiPlasmid = stack
			} else if (stack.isNotEmpty()) {
				return false
			}
		}

		return antiPlasmid != null
	}

	override fun assemble(input: CraftingInput): ItemStack {
		return ModItems.ANTI_PLASMID.toStack()
	}

	override fun getSerializer(): RecipeSerializer<UnsetAntiPlasmidRecipe> {
		return ModRecipeSerializers.UNSET_ANTI_PLASMID.get()
	}

	companion object {
		val CODEC: MapCodec<UnsetAntiPlasmidRecipe> = MapCodec.unit(UnsetAntiPlasmidRecipe())

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, UnsetAntiPlasmidRecipe> =
			StreamCodec.unit(UnsetAntiPlasmidRecipe())
	}

}
