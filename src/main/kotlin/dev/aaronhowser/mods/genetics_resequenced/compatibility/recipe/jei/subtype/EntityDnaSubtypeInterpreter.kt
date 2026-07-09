package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype

import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.world.item.ItemStack

object EntityDnaSubtypeInterpreter : ISubtypeInterpreter<ItemStack> {

	override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? {
		return ingredient.get(ModDataComponents.ENTITY_TYPE)
	}

}
