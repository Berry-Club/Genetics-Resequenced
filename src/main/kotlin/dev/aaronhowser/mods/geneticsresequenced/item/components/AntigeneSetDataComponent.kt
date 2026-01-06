package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderSet

data class AntigeneSetDataComponent(
	val antigenes: HolderSet<Gene>
) : PseudoDataComponent<AntigeneSetDataComponent, AntigeneSetDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<AntigeneSetDataComponent>(OtherUtil.modResource("antigenes")) {
		val CODEC: Codec<AntigeneSetDataComponent> = Gene.HOLDER_SET_CODEC
			.xmap(::AntigeneSetDataComponent, AntigeneSetDataComponent::antigenes)

		override fun getCodec(): Codec<AntigeneSetDataComponent> = CODEC
	}

	override val type: Type = Type
}