package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.subtype

import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.world.item.ItemStack

object GeneSubtypeInterpreter : ISubtypeInterpreter<ItemStack> {

	override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? {
		return ingredient.get(ModDataComponents.GENE)?.key
	}

	@Suppress("OVERRIDE_DEPRECATION")
	override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String = ""

}
