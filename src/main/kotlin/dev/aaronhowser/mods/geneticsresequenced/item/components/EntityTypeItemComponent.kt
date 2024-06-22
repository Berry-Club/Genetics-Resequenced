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

data class EntityTypeItemComponent(
    val entity: EntityType<*>
) {

    companion object {

        val CODEC: Codec<EntityTypeItemComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(EntityTypeItemComponent::entity)
            ).apply(instance, ::EntityTypeItemComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EntityTypeItemComponent> = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), EntityTypeItemComponent::entity,
            ::EntityTypeItemComponent
        )

        val component: DataComponentType<EntityTypeItemComponent> by lazy { ModDataComponents.ENTITY_TYPE_COMPONENT.get() }

    }

}