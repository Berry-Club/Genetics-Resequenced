package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType

data class EntityTypeComponent(
    val entity: EntityType<*>
) {

    companion object {

        fun fromString(string: String): EntityTypeComponent {
            val resourceLocation = ResourceLocation.tryParse(string)
                ?: throw IllegalArgumentException("Invalid Resource Location: $string")

            val entityType = OtherUtil.getEntityType(resourceLocation)

            return EntityTypeComponent(entityType)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, EntityTypeComponent> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ::fromString,
            EntityTypeComponent::entity
        )

        val CODEC: Codec<EntityTypeComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity")
                    .forGetter(EntityTypeComponent::entity)
            ).apply(instance, ::EntityTypeComponent)
        }

    }

}