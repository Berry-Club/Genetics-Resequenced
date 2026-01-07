package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.resources.ResourceKey

data class PlasmidProgressItemComponent(
	val geneRK: ResourceKey<Gene>,
	val dnaPoints: Int
) : PseudoDataComponent<PlasmidProgressItemComponent, PlasmidProgressItemComponent.Type>() {

	object Type : PseudoDataComponent.Type<PlasmidProgressItemComponent>(OtherUtil.modResource("plasmid_progress")) {
		val CODEC: Codec<PlasmidProgressItemComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
						.fieldOf("gene")
						.forGetter(PlasmidProgressItemComponent::geneRK),
					Codec.INT
						.fieldOf("dna_points")
						.forGetter(PlasmidProgressItemComponent::dnaPoints)
				).apply(instance, ::PlasmidProgressItemComponent)
			}

		override fun getCodec(): Codec<PlasmidProgressItemComponent> = CODEC
	}

	override val type: Type = Type

}