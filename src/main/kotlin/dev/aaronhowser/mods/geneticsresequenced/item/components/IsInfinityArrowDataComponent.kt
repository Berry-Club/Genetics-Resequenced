package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil

data class IsInfinityArrowDataComponent(
	val isInfinityArrow: Boolean
) : PseudoDataComponent<IsInfinityArrowDataComponent, IsInfinityArrowDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<IsInfinityArrowDataComponent>(OtherUtil.modResource("is_infinity_arrow")) {
		val CODEC: Codec<IsInfinityArrowDataComponent> = Codec.BOOL
			.xmap(::IsInfinityArrowDataComponent, IsInfinityArrowDataComponent::isInfinityArrow)

		override fun getCodec(): Codec<IsInfinityArrowDataComponent> = CODEC
	}

	override val type: Type = Type
}