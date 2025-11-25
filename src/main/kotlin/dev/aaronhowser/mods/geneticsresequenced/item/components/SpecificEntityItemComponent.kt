package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.AaronCodecs
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.UUIDUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import java.util.*

data class SpecificEntityItemComponent(
	val uuid: UUID,
	val name: Component
) : PseudoDataComponent<SpecificEntityItemComponent, SpecificEntityItemComponent.Type>() {

	object Type : PseudoDataComponent.Type<SpecificEntityItemComponent>(OtherUtil.modResource("specific_entity")) {
		val CODEC: Codec<SpecificEntityItemComponent> = RecordCodecBuilder.create { instance ->
			instance.group(
				UUIDUtil.CODEC
					.fieldOf("uuid")
					.forGetter(SpecificEntityItemComponent::uuid),
				AaronCodecs.COMPONENT_CODEC
					.fieldOf("name")
					.forGetter(SpecificEntityItemComponent::name)
			).apply(instance, ::SpecificEntityItemComponent)
		}

		override fun getCodec(): Codec<SpecificEntityItemComponent> = CODEC
	}

	override val type: Type = Type

	companion object {
		fun setEntity(stack: ItemStack, entity: LivingEntity) {
			val name = entity.name
			val uuid = entity.uuid

			val entityComponent = SpecificEntityItemComponent(uuid, name)
			stack.setComponent(entityComponent)
		}

		fun hasEntity(stack: ItemStack): Boolean = stack.hasComponent(Type)
		fun getEntityUuid(stack: ItemStack): UUID? = stack.getComponent(Type)?.uuid
		fun getEntityName(stack: ItemStack): Component? = stack.getComponent(Type)?.name

	}

}