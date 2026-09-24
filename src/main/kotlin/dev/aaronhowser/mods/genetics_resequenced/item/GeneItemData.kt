package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.util.ItemStackNbt
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

object GeneItemData {
	private const val GENE = "genetics_resequenced:gene"

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