package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.subtypes

import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.world.item.ItemStack

class DnaHelixSubtypeInterpreter : ISubtypeInterpreter<ItemStack> {

	override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? {
		return DnaHelixItem.getGeneHolder(ingredient)
	}

	override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String {
		val geneHolder = DnaHelixItem.getGeneHolder(ingredient)
		return geneHolder?.registeredName ?: ""
	}
}