package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced

data class IsContaminatedDataComponent(
	val isContaminated: Boolean
) : PseudoDataComponent<IsContaminatedDataComponent, IsContaminatedDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<IsContaminatedDataComponent>(GeneticsResequenced.modResource("is_contaminated")) {
		val CODEC: Codec<IsContaminatedDataComponent> = Codec.BOOL
			.xmap(::IsContaminatedDataComponent, IsContaminatedDataComponent::isContaminated)

		override fun getCodec(): Codec<IsContaminatedDataComponent> = CODEC
	}

	override val type: Type = Type
}