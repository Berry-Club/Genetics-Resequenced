package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.UUIDUtil
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import java.util.*

data class SpecificEntityItemComponent(
	val uuid: UUID,
	val name: Component
) : PseudoDataComponent<SpecificEntityItemComponent, SpecificEntityItemComponent.Type>() {

	class Type : PseudoDataComponent.Type<SpecificEntityItemComponent>(OtherUtil.modResource("specific_entity")) {
		override fun getCodec(): Codec<SpecificEntityItemComponent> = CODEC

		companion object {
			val CODEC: Codec<SpecificEntityItemComponent> = RecordCodecBuilder.create { instance ->
				instance.group(
					UUIDUtil.CODEC
						.fieldOf("uuid")
						.forGetter(SpecificEntityItemComponent::uuid),
					ComponentSerialization.CODEC
						.fieldOf("name")
						.forGetter(SpecificEntityItemComponent::name)
				).apply(instance, ::SpecificEntityItemComponent)
			}
		}

	}

	companion object {
		fun setEntity(stack: ItemStack, entity: LivingEntity) {
			val name = entity.name
			val uuid = entity.uuid

			val entityComponent = SpecificEntityItemComponent(uuid, name)

			stack.set(ModDataComponents.SPECIFIC_ENTITY, entityComponent)
		}

		fun hasEntity(stack: ItemStack): Boolean = stack.has(ModDataComponents.SPECIFIC_ENTITY)
		fun getEntityUuid(stack: ItemStack): UUID? = stack.get(ModDataComponents.SPECIFIC_ENTITY)?.uuid
		fun getEntityName(stack: ItemStack): Component? = stack.get(ModDataComponents.SPECIFIC_ENTITY)?.name

	}

}