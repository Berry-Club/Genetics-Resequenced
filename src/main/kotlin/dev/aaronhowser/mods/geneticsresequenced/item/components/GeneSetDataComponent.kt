package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderSet

data class GeneSetDataComponent(
	val genes: HolderSet<Gene>
) : PseudoDataComponent<GeneSetDataComponent, GeneSetDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<GeneSetDataComponent>(OtherUtil.modResource("genes")) {
		val CODEC: Codec<GeneSetDataComponent> = Gene.HOLDER_SET_CODEC
			.xmap(::GeneSetDataComponent, GeneSetDataComponent::genes)

		override fun getCodec(): Codec<GeneSetDataComponent> = CODEC
	}

	override val type: Type = Type
}