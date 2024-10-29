package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import java.util.*

data class SpecificEntityItemComponent(
    val uuid: UUID,
    val name: String
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

        val CODEC: Codec<SpecificEntityItemComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                NeoForgeExtraCodecs
                    .aliasedFieldOf(UUID_CODEC, "uuid", "entityUuid", "entity_uuid")
                    .forGetter(SpecificEntityItemComponent::uuid),
                NeoForgeExtraCodecs
                    .aliasedFieldOf(Codec.STRING, "name", "entityName", "entity_name")
                    .forGetter(SpecificEntityItemComponent::name)
            ).apply(instance, ::SpecificEntityItemComponent)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, SpecificEntityItemComponent> = StreamCodec.composite(
            UUID_STREAM_CODEC, SpecificEntityItemComponent::uuid,
            ByteBufCodecs.STRING_UTF8, SpecificEntityItemComponent::name,
            ::SpecificEntityItemComponent
        )

        fun ItemStack.setEntity(entity: LivingEntity) {
            val name = entity.name.string
            val uuid = entity.uuid

            val entityComponent = SpecificEntityItemComponent(uuid, name)

            this.set(ModDataComponents.SPECIFIC_ENTITY_COMPONENT, entityComponent)
        }

        fun ItemStack.hasEntity(): Boolean {
            return this.has(ModDataComponents.SPECIFIC_ENTITY_COMPONENT)
        }

        fun ItemStack.getEntityUuid(): UUID? {
            return this.get(ModDataComponents.SPECIFIC_ENTITY_COMPONENT)?.uuid
        }

        fun ItemStack.getEntityName(): String? {
            return this.get(ModDataComponents.SPECIFIC_ENTITY_COMPONENT)?.name
        }

    }

}