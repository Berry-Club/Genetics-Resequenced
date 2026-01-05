package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil

data class DragonHealthCrystalDamageDataComponent(
	val damageRemaining: Float
) : PseudoDataComponent<DragonHealthCrystalDamageDataComponent, DragonHealthCrystalDamageDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<DragonHealthCrystalDamageDataComponent>(OtherUtil.modResource("dragon_health_crystal_damage")) {
		val CODEC: Codec<DragonHealthCrystalDamageDataComponent> = Codec.FLOAT
			.xmap(::DragonHealthCrystalDamageDataComponent, DragonHealthCrystalDamageDataComponent::damageRemaining)

		override fun getCodec(): Codec<DragonHealthCrystalDamageDataComponent> = CODEC
	}

	override val type: Type = Type
}