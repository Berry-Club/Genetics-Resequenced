package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import java.util.*

data class SpecificEntityComponent(
    val entityUuid: UUID,
    val entityName: String
) {

    companion object {

        @Suppress("MemberVisibilityCanBePrivate")
        val UUID_CODEC: Codec<UUID> = Codec.STRING.xmap(
            { UUID.fromString(it) },
            { it.toString() }
        )

        @Suppress("MemberVisibilityCanBePrivate")
        val UUID_STREAM_CODEC: StreamCodec<ByteBuf, UUID> = ByteBufCodecs.STRING_UTF8.map(
            { UUID.fromString(it) },
            { it.toString() }
        )

        val CODEC: Codec<SpecificEntityComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                UUID_CODEC.fieldOf("entityUuid").forGetter(SpecificEntityComponent::entityUuid),
                Codec.STRING.fieldOf("entityName").forGetter(SpecificEntityComponent::entityName)
            ).apply(instance, ::SpecificEntityComponent)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, SpecificEntityComponent> = StreamCodec.composite(
            UUID_STREAM_CODEC, SpecificEntityComponent::entityUuid,
            ByteBufCodecs.STRING_UTF8, SpecificEntityComponent::entityName,
            ::SpecificEntityComponent
        )

        val component: DataComponentType<SpecificEntityComponent> by lazy { ModDataComponents.SPECIFIC_ENTITY_COMPONENT.get() }

        fun ItemStack.setEntity(entity: LivingEntity) {
            val name = entity.name.string
            val uuid = entity.uuid

            val entityComponent = SpecificEntityComponent(uuid, name)

            this.set(component, entityComponent)
        }

        fun ItemStack.hasEntity(): Boolean {
            return this.has(component)
        }

        fun ItemStack.getEntityUuid(): UUID? {
            return this.get(component)?.entityUuid
        }

        fun ItemStack.getEntityName(): String? {
            return this.get(component)?.entityName
        }

    }

}