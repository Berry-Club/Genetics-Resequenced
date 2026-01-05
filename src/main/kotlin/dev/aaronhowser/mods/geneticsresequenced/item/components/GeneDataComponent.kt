package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder

data class GeneDataComponent(
	val geneHolder: Holder<Gene>
) : PseudoDataComponent<GeneDataComponent, GeneDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<GeneDataComponent>(OtherUtil.modResource("gene")) {
		val CODEC: Codec<GeneDataComponent> = Gene.CODEC
			.xmap(::GeneDataComponent, GeneDataComponent::geneHolder)

		override fun getCodec(): Codec<GeneDataComponent> = CODEC
	}

	override val type: Type = Type
}