package dev.aaronhowser.mods.genetics_resequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import java.util.*

data class SpecificEntityItemComponent(
	val uuid: UUID,
	val name: Component
) {

	companion object {

		val CODEC: Codec<SpecificEntityItemComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					NeoForgeExtraCodecs
						.aliasedFieldOf(UUIDUtil.CODEC, "uuid", "entityUuid", "entity_uuid")
						.forGetter(SpecificEntityItemComponent::uuid),
					NeoForgeExtraCodecs
						.aliasedFieldOf(ComponentSerialization.CODEC, "name", "entityName", "entity_name")
						.forGetter(SpecificEntityItemComponent::name)
				).apply(instance, ::SpecificEntityItemComponent)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SpecificEntityItemComponent> =
			StreamCodec.composite(
				UUIDUtil.STREAM_CODEC, SpecificEntityItemComponent::uuid,
				ComponentSerialization.STREAM_CODEC, SpecificEntityItemComponent::name,
				::SpecificEntityItemComponent
			)

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