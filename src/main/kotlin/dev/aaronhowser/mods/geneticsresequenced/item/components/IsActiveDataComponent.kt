package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil

data class IsActiveDataComponent(
	val isActive: Boolean
) : PseudoDataComponent<IsActiveDataComponent, IsActiveDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<IsActiveDataComponent>(OtherUtil.modResource("is_active")) {
		val CODEC: Codec<IsActiveDataComponent> = Codec.BOOL
			.xmap(::IsActiveDataComponent, IsActiveDataComponent::isActive)

		override fun getCodec(): Codec<IsActiveDataComponent> = CODEC
	}

	override val type: Type = Type
}