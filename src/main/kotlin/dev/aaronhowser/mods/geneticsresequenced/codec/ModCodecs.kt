package dev.aaronhowser.mods.geneticsresequenced.codec

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene.Companion.fromId
import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeComponent
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation

sealed interface ModCodecs {


    val Gene = object {
        val gene: Codec<Gene> = ResourceLocation.CODEC.xmap(
            { id: ResourceLocation -> fromId(id) ?: throw IllegalArgumentException("Unknown gene $id") },
            { gene: Gene -> gene.id }
        )
    }

    val Boolean = object  {
        val CODEC: Codec<BooleanComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BOOL.fieldOf("value").forGetter(BooleanComponent::value)
            ).apply(instance, ::BooleanComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BooleanComponent> = StreamCodec.composite(
            ByteBufCodecs.BOOL, BooleanComponent::value,
            ::BooleanComponent
        )
    }

    val entityType: Codec<EntityTypeComponent> = RecordCodecBuilder.create { instance ->
        instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(EntityTypeComponent::entity)
        ).apply(instance, ::EntityTypeComponent)
    }

    val entityType_stream: StreamCodec<RegistryFriendlyByteBuf, EntityTypeComponent> = StreamCodec.composite(
        ByteBufCodecs.registry(Registries.ENTITY_TYPE), EntityTypeComponent::entity,
        ::EntityTypeComponent
    )

}