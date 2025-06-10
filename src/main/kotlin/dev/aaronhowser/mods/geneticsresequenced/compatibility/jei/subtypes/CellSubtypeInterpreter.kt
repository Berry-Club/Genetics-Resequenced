package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.subtypes

import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.world.item.ItemStack

class CellSubtypeInterpreter : ISubtypeInterpreter<ItemStack> {

	override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? {
		return EntityDnaItem.getEntityType(ingredient)
	}

	override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String {
		val entityType = EntityDnaItem.getEntityType(ingredient)
		return entityType?.descriptionId ?: ""
	}

}