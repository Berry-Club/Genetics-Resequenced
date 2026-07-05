package dev.aaronhowser.mods.genetics_resequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs

data class PlasmidProgressItemComponent(
	val geneHolder: Holder<Gene>,
	val dnaPoints: Int
) {

	companion object {

		val CODEC: Codec<PlasmidProgressItemComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Gene.CODEC
						.fieldOf("gene")
						.forGetter(PlasmidProgressItemComponent::geneHolder),
					NeoForgeExtraCodecs
						.aliasedFieldOf(Codec.INT, "dna_points", "dnaPoints")
						.forGetter(PlasmidProgressItemComponent::dnaPoints)
				).apply(instance, ::PlasmidProgressItemComponent)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, PlasmidProgressItemComponent> =
			StreamCodec.composite(
				Gene.STREAM_CODEC, PlasmidProgressItemComponent::geneHolder,
				ByteBufCodecs.INT, PlasmidProgressItemComponent::dnaPoints,
				::PlasmidProgressItemComponent
			)
	}

}