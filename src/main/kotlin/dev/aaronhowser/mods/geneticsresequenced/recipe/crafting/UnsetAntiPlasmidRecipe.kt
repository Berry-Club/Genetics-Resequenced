package dev.aaronhowser.mods.geneticsresequenced.recipe.crafting

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class UnsetAntiPlasmidRecipe(
	id: ResourceLocation = GeneticsResequenced.modResource("unset_anti_plasmid"),
	craftingCategory: CraftingBookCategory = CraftingBookCategory.MISC
) : CustomRecipe(id, craftingCategory) {

	override fun matches(input: CraftingContainer, level: Level): Boolean {
		var antiPlasmid: ItemStack? = null

		for (stack in input.items) {
			if (stack.item == ModItems.ANTI_PLASMID.get() && PlasmidItem.hasGene(stack)) {
				if (antiPlasmid != null) return false
				antiPlasmid = stack
			} else if (stack.isNotEmpty()) {
				return false
			}
		}

		return antiPlasmid != null
	}

	override fun assemble(pContainer: CraftingContainer, pRegistryAccess: RegistryAccess): ItemStack {
		return ModItems.ANTI_PLASMID.getDefaultInstance()
	}

	override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
		return pWidth * pHeight >= 1
	}

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.UNSET_ANTI_PLASMID.get()
	}

}