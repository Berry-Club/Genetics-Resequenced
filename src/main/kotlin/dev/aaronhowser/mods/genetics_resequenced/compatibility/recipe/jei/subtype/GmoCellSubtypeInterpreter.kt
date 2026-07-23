package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.jei.subtype

import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack

object GmoCellSubtypeInterpreter : ISubtypeInterpreter<ItemStack> {

	override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? {
		val entityType = ingredient.get(ModDataComponents.ENTITY_TYPE)
		val gene = ingredient.get(ModDataComponents.GENE)?.key

		if (entityType == null && gene == null) return null

		return SubtypeData(entityType, gene)
	}

	private data class SubtypeData(
		val entityType: EntityType<*>?,
		val gene: ResourceKey<Gene>?
	)

}
