package dev.aaronhowser.mods.geneticsresequenced.attachment

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity

data class TemporaryGenesData(
	private val temporaryGenes: MutableList<TemporaryGene>
) {

	constructor() : this(mutableListOf())

	companion object {
		val CODEC: Codec<TemporaryGenesData> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					TemporaryGene.CODEC
						.listOf()
						.fieldOf("temporary_genes")
						.forGetter(TemporaryGenesData::temporaryGenes)
				).apply(instance, ::TemporaryGenesData)
			}

		@JvmStatic
		var LivingEntity.temporaryGeneHolders: List<TemporaryGene>
			get() = this.getData(ModAttachmentTypes.TEMPORARY_GENES).temporaryGenes
			private set(value) {
				this.setData(ModAttachmentTypes.TEMPORARY_GENES, TemporaryGenesData(value.toMutableList()))
			}

		fun addTemporaryGene(
			entity: LivingEntity,
			newGeneHolder: Holder<Gene>,
			durationTicks: Int
		) {
			val list = entity.temporaryGeneHolders.toMutableList()

			val existing = list.find { it.geneHolder.isGene(newGeneHolder)}
			if (existing != null) {
				existing.ticksRemaining = durationTicks
			} else {
				list.add(TemporaryGene(newGeneHolder, durationTicks))
			}

			entity.temporaryGeneHolders = list
		}
	}

	class TemporaryGene(
		val geneHolder: Holder<Gene>,
		var ticksRemaining: Int
	) {
		fun tick(): Boolean {
			ticksRemaining--
			return ticksRemaining <= 0
		}

		companion object {
			val CODEC: Codec<TemporaryGene> =
				RecordCodecBuilder.create { instance ->
					instance.group(
						Gene.CODEC
							.fieldOf("gene")
							.forGetter(TemporaryGene::geneHolder),
						Codec.INT
							.fieldOf("ticks_remaining")
							.forGetter(TemporaryGene::ticksRemaining)
					).apply(instance, ::TemporaryGene)
				}
		}
	}

}