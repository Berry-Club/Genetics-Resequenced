package dev.aaronhowser.mods.genetics_resequenced.recipe.crafting

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.genetics_resequenced.item.PlasmidItem
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.registry.ModRecipeSerializers
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingInput
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class SetAntiPlasmidRecipe private constructor() : CustomRecipe() {

	override fun matches(input: CraftingInput, level: Level): Boolean {
		var plasmid: ItemStack? = null
		var antiPlasmid: ItemStack? = null

		for (stack in input.items()) {
			if (stack.item == ModItems.PLASMID.get()) {
				if (plasmid != null) return false
				plasmid = stack
			}
			if (stack.item == ModItems.ANTI_PLASMID.get()) {
				if (antiPlasmid != null) return false
				antiPlasmid = stack
			}
			if (plasmid != null && antiPlasmid != null) break
		}

		if (plasmid == null || antiPlasmid == null) return false

		return !PlasmidItem.hasGene(antiPlasmid) && PlasmidItem.isComplete(plasmid)
	}

	override fun assemble(input: CraftingInput): ItemStack {
		var plasmidStack: ItemStack? = null
		for (stack in input.items()) {
			if (stack.item == ModItems.PLASMID.get()) {
				if (plasmidStack != null) return ItemStack.EMPTY
				plasmidStack = stack
			}
		}
		if (plasmidStack == null) return ItemStack.EMPTY

		val component = plasmidStack.get(ModDataComponents.PLASMID_PROGRESS) ?: return ItemStack.EMPTY
		val antiPlasmidStack = ModItems.ANTI_PLASMID.withComponent(ModDataComponents.PLASMID_PROGRESS.get(), component)

		return antiPlasmidStack
	}

	override fun getSerializer(): RecipeSerializer<SetAntiPlasmidRecipe> {
		return ModRecipeSerializers.SET_ANTI_PLASMID.get()
	}

	companion object {
		val INSTANCE = SetAntiPlasmidRecipe()

		val CODEC: MapCodec<SetAntiPlasmidRecipe> =
			MapCodec.unit(INSTANCE)

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetAntiPlasmidRecipe> =
			StreamCodec.unit(INSTANCE)
	}

}
