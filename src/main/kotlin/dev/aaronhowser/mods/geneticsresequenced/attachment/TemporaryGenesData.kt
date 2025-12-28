package dev.aaronhowser.mods.geneticsresequenced.attachment

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isHelixOnly
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import net.minecraft.core.Holder
import net.minecraft.world.entity.EntityType
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
		var LivingEntity.temporaryGenes: List<TemporaryGene>
			get() = this.getData(ModAttachmentTypes.TEMPORARY_GENES).temporaryGenes
			private set(value) {
				this.setData(ModAttachmentTypes.TEMPORARY_GENES, TemporaryGenesData(value.toMutableList()))
			}

		@JvmStatic
		val LivingEntity.temporaryGeneHolders: List<Holder<Gene>>
			get() = this.temporaryGenes.map(TemporaryGene::geneHolder)

		@JvmStatic
		fun LivingEntity.addTemporaryGene(
			newGeneHolder: Holder<Gene>,
			durationTicks: Int
		): Boolean {
			if (newGeneHolder.isHelixOnly) {
				GeneticsResequenced.LOGGER.debug(
					"Cannot add gene $newGeneHolder to entities, as it has tag `#geneticsresequenced:helix_only`."
				)
				return false
			}

			val allowedTypes = newGeneHolder.value().allowedEntities.map(Holder<EntityType<*>>::value)
			if (this.type !in allowedTypes) {
				GeneticsResequenced.LOGGER.debug(
					StringBuilder()
						.append("Tried to give temporary gene ")
						.append(newGeneHolder.key?.location() ?: newGeneHolder)
						.append(" to entity ").append(name.string)
						.append(", but that entity type cannot have that gene!")
						.toString()
				)
				return false
			}

			val list = this.temporaryGenes.toMutableList()

			val existing = list.find { it.geneHolder.isGene(newGeneHolder) }
			if (existing != null) {
				existing.ticksRemaining = durationTicks
			} else {
				list.add(TemporaryGene(newGeneHolder, durationTicks))
			}

			this.temporaryGenes = list

			return true
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