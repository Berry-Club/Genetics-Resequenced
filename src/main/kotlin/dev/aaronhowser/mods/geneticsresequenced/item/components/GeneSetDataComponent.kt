package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack

data class GeneSetDataComponent(
	val genes: List<ResourceKey<Gene>>
) : PseudoDataComponent<GeneSetDataComponent, GeneSetDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<GeneSetDataComponent>(GeneticsResequenced.modResource("genes")) {
		val CODEC: Codec<GeneSetDataComponent> =
			ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
				.listOf()
				.xmap(::GeneSetDataComponent, GeneSetDataComponent::genes)

		override fun getCodec(): Codec<GeneSetDataComponent> = CODEC
	}

	override val type: Type = Type

	companion object {

		fun getGenes(itemStack: ItemStack): List<ResourceKey<Gene>> = itemStack.getComponent(Type)?.genes ?: emptyList()

		fun setGenes(itemStack: ItemStack, geneRks: List<ResourceKey<Gene>>): ItemStack {
			itemStack.setComponent(GeneSetDataComponent(geneRks))
			return itemStack
		}

	}

}