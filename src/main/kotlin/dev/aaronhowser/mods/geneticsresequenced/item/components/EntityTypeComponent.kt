package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.EntityType

data class EntityTypeComponent(
    val entity: EntityType<*>
) {

    companion object {

        val CODEC: Codec<EntityTypeComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(EntityTypeComponent::entity)
            ).apply(instance, ::EntityTypeComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EntityTypeComponent> = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), EntityTypeComponent::entity,
            ::EntityTypeComponent
        )

        val component: DataComponentType<EntityTypeComponent> by lazy { ModDataComponents.ENTITY_TYPE_COMPONENT.get() }

    }

}