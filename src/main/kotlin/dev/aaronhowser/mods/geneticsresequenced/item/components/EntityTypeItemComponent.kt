package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.EntityType

data class EntityTypeItemComponent(
    val entity: EntityType<*>
) {

    companion object {

        val CODEC: Codec<EntityTypeItemComponent> =
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().xmap(::EntityTypeItemComponent, EntityTypeItemComponent::entity)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EntityTypeItemComponent> =
            ByteBufCodecs.registry(Registries.ENTITY_TYPE)
                .map(::EntityTypeItemComponent, EntityTypeItemComponent::entity)

    }

}