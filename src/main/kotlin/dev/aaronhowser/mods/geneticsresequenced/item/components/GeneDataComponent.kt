package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack

data class GeneDataComponent(
	val geneRk: ResourceKey<Gene>
) : PseudoDataComponent<GeneDataComponent, GeneDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<GeneDataComponent>(OtherUtil.modResource("gene")) {
		val CODEC: Codec<GeneDataComponent> = ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
			.xmap(::GeneDataComponent, GeneDataComponent::geneRk)

		override fun getCodec(): Codec<GeneDataComponent> = CODEC
	}

	override val type: Type = Type

	companion object {
		fun hasGene(itemStack: ItemStack): Boolean = itemStack.hasComponent(Type)

		fun getGeneRk(itemStack: ItemStack): ResourceKey<Gene>? = itemStack.getComponent(Type)?.geneRk

		fun getGeneHolder(registries: HolderLookup.Provider, itemStack: ItemStack): Holder<Gene>? {
			val rk = getGeneRk(itemStack) ?: return null
			return ModGenes.getGeneRegistry(registries).getOrThrow(rk)
		}

		fun setGene(itemStack: ItemStack, geneHolder: Holder<Gene>): ItemStack {
			setGene(itemStack, geneHolder.unwrapKey().get())
			return itemStack
		}

		fun setGene(itemStack: ItemStack, geneRk: ResourceKey<Gene>): ItemStack {
			itemStack.setComponent(GeneDataComponent(geneRk))
			return itemStack
		}
	}
}