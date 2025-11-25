package dev.aaronhowser.mods.geneticsresequenced.capability

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.Holder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.common.capabilities.CapabilityToken

class GenesContainer : IGenesContainer {
	override var genes: MutableSet<Holder<Gene>> = mutableSetOf()

	override fun add(gene: Holder<Gene>): Boolean = genes.add(gene)
	override fun remove(gene: Holder<Gene>): Boolean = genes.remove(gene)
	override fun has(gene: Holder<Gene>): Boolean = genes.contains(gene)

	fun toTag(): CompoundTag {
		val tag = CompoundTag()
		val list = ListTag()

		for (gene in genes) {
			if (gene !is Holder.Reference) continue
			list.add(StringTag.valueOf(gene.key().location().toString()))
		}

		tag.put(GENE_LIST_NBT, list)
		return tag
	}

	fun fromTag(tag: CompoundTag) {
		genes.clear()
		val list = tag.getList(GENE_LIST_NBT, ListTag.TAG_STRING.toInt())

		for (i in 0 until list.size) {
			val geneString = list.getString(i)
			val geneHolder = ModGenes.fromString()

			if (geneHolder != null) {
				genes.add(geneHolder)
			}
		}
	}

	companion object {
		private const val GENE_LIST_NBT = "Genes"
		val CAPABILITY: Capability<GenesContainer> =
			CapabilityManager.get(object : CapabilityToken<GenesContainer>() {})
	}

}