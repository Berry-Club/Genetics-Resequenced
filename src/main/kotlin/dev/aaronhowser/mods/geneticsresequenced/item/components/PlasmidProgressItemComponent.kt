package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder

data class PlasmidProgressItemComponent(
	val geneHolder: Holder<Gene>,
	val dnaPoints: Int
) : PseudoDataComponent<PlasmidProgressItemComponent, PlasmidProgressItemComponent.Type>() {

	class Type : PseudoDataComponent.Type<PlasmidProgressItemComponent>(OtherUtil.modResource("plasmid_progress")) {
		override fun getCodec(): Codec<PlasmidProgressItemComponent> = CODEC

		companion object {
			val CODEC: Codec<PlasmidProgressItemComponent> = RecordCodecBuilder.create { instance ->
				instance.group(
					Gene.CODEC
						.fieldOf("gene")
						.forGetter(PlasmidProgressItemComponent::geneHolder),
					// Should make it start writing as dna_points, but won't break existing saves
					Codec.INT
						.optionalFieldOf("dna_points", 0)
						.forGetter(PlasmidProgressItemComponent::dnaPoints)
				).apply(instance, ::PlasmidProgressItemComponent)
			}
		}
	}

	override val type: Type = Type()

}