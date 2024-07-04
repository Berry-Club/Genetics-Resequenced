package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec

data class GeneItemComponent(
    val gene: Gene
) {

    companion object {

        val CODEC: Codec<GeneItemComponent> =
            Gene.CODEC.xmap(::GeneItemComponent, GeneItemComponent::gene)

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GeneItemComponent> =
            Gene.STREAM_CODEC.map(::GeneItemComponent, GeneItemComponent::gene)

        val component: DataComponentType<GeneItemComponent> by lazy { ModDataComponents.GENE_COMPONENT.get() }

    }

}