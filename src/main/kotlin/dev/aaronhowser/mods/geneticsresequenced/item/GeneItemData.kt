package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.ItemStackNbt
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

object GeneItemData {
	private const val GENE = "geneticsresequenced:gene"

	fun hasGene(itemStack: ItemStack): Boolean {
		return ItemStackNbt.getString(itemStack, GENE) != null
	}

	fun getGeneRk(itemStack: ItemStack): ResourceKey<Gene>? {
		val geneId = ItemStackNbt.getString(itemStack, GENE)
		if (geneId.isNullOrEmpty()) return null
		val geneLocation = ResourceLocation.tryParse(geneId) ?: return null

		return ResourceKey.create(
			ModGenes.GENE_REGISTRY_KEY,
			geneLocation
		)
	}

	fun setGene(itemStack: ItemStack, geneHolder: Holder<Gene>): ItemStack {
		setGene(itemStack, geneHolder.unwrapKey().get())
		return itemStack
	}

	fun setGene(itemStack: ItemStack, geneResourceKey: ResourceKey<Gene>): ItemStack {
		ItemStackNbt.putString(itemStack, GENE, geneResourceKey.location().toString())
		return itemStack
	}
}