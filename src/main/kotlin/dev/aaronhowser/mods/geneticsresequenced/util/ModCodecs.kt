package dev.aaronhowser.mods.geneticsresequenced.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.UUIDUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import java.util.Optional

object ModCodecs {

	val COMPONENT: Codec<Component> = Codec.STRING.xmap(
		Component.Serializer::fromJson,
		Component.Serializer::toJson
	)

	val ATTRIBUTE_MODIFIER: Codec<AttributeModifier> = RecordCodecBuilder.create { instance ->
		instance.group(
			UUIDUtil.CODEC
				.optionalFieldOf("uuid")
				.forGetter { modifier -> Optional.of(modifier.id) },
			Codec.STRING
				.fieldOf("name")
				.forGetter(AttributeModifier::getName),
			Codec.DOUBLE
				.fieldOf("amount")
				.forGetter(AttributeModifier::getAmount),
			Codec.INT
				.fieldOf("operation")
				.forGetter { modifier -> modifier.operation.toValue() }
		).apply(instance) { uuid, name, amount, operationValue ->
			val operation = AttributeModifier.Operation.fromValue(operationValue)

			if (uuid.isPresent) {
				AttributeModifier(uuid.get(), name, amount, operation)
			} else {
				AttributeModifier(name, amount, operation)
			}
		}
	}
}