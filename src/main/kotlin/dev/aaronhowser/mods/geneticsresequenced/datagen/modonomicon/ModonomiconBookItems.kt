package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.util.Unit
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

object ModonomiconBookItems {

	fun activeAntiFieldOrb(): ItemStack {
		val stack = ModItems.ANTI_FIELD_ORB.toStack()
		stack.set(ModDataComponents.IS_ACTIVE, Unit.INSTANCE)
		return stack
	}

	fun entityStack(item: ItemLike, entityType: EntityType<*>): ItemStack {
		return entityStack(item.itemStack, entityType)
	}

	fun entityStack(stack: ItemStack, entityType: EntityType<*>): ItemStack {
		stack.set(ModDataComponents.ENTITY_TYPE, entityType)
		return stack
	}

	fun geneStack(
		registries: HolderLookup.Provider,
		item: ItemLike,
		geneKey: ResourceKey<Gene>
	): ItemStack {
		val stack = item.itemStack
		stack.set(ModDataComponents.GENE, geneHolder(registries, geneKey))
		return stack
	}

	fun gmoCellStack(
		registries: HolderLookup.Provider,
		geneKey: ResourceKey<Gene>,
		entityType: EntityType<*>
	): ItemStack {
		val stack = geneStack(registries, ModItems.GMO_CELL.get(), geneKey)
		return entityStack(stack, entityType)
	}

	fun plasmidStack(
		registries: HolderLookup.Provider,
		geneKey: ResourceKey<Gene>,
		dnaPoints: Int
	): ItemStack {
		val stack = ModItems.PLASMID.toStack()
		PlasmidItem.setGene(stack, geneHolder(registries, geneKey), dnaPoints)
		return stack
	}

	private fun geneHolder(
		registries: HolderLookup.Provider,
		geneKey: ResourceKey<Gene>
	): Holder.Reference<Gene> {
		return ModGenes
			.getGeneRegistry(registries)
			.getOrThrow(geneKey)
	}
}