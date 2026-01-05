package dev.aaronhowser.mods.geneticsresequenced.capability

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.AaronExtensions.getLocationOrNull
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import kotlin.jvm.optionals.getOrNull

class TemporaryGenesCapability() {

	constructor(temporaryGenes: List<TemporaryGene>) : this() {
		this.temporaryGenes = temporaryGenes
	}

	private var temporaryGenes: List<TemporaryGene> = listOf()

	fun toTag(registries: HolderLookup.Provider): CompoundTag {
		val tag = CompoundTag()

		val listTag = ListTag()
		for (tempGene in temporaryGenes) {
			val tempGeneTag = tempGene.toTag(registries)
			listTag.add(tempGeneTag)
		}

		tag.put(TEMPORARY_GENES_TAG, listTag)
		return tag
	}

	fun fromTag(registries: HolderLookup.Provider, tag: CompoundTag) {
		val listTag = tag.getList(TEMPORARY_GENES_TAG, Tag.TAG_COMPOUND.toInt())

		val newTempGenes = mutableListOf<TemporaryGene>()

		for (i in listTag.indices) {
			val tempGeneTag = listTag.getCompound(i)
			try {
				val tempGene = TemporaryGene.fromTag(registries, tempGeneTag)
				newTempGenes.add(tempGene)
			} catch (e: IllegalArgumentException) {
				GeneticsResequenced.LOGGER.warn("Could not load TemporaryGene from NBT! Skipping...", e)
			}
		}

		temporaryGenes = newTempGenes
	}

	companion object {
		private const val TEMPORARY_GENES_TAG = "temporary_genes"

		val CODEC: Codec<TemporaryGenesCapability> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					TemporaryGene.CODEC
						.listOf()
						.fieldOf("temporary_genes")
						.forGetter(TemporaryGenesCapability::temporaryGenes)
				).apply(instance, ::TemporaryGenesCapability)
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

		fun getComponent(): Component {
			return ModLanguageProvider.Commands.TEMPORARY_GENE_WITH_DURATION.toComponent(
				geneHolder.getName(),
				ticksRemaining
			)
		}

		fun toTag(registries: HolderLookup.Provider): CompoundTag {
			val tag = CompoundTag()
			val geneLocation = geneHolder.getLocationOrNull() ?: return tag
			tag.putString(GENE_TAG, geneLocation.toString())
			tag.putInt(TICKS_REMAINING_TAG, ticksRemaining)
			return tag
		}

		companion object {
			private const val GENE_TAG = "gene"
			private const val TICKS_REMAINING_TAG = "ticks_remaining"

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

			fun fromTag(registries: HolderLookup.Provider, tag: CompoundTag): TemporaryGene {
				val registry = registries.lookupOrThrow(ModGenes.GENE_REGISTRY_KEY)

				val geneLocation = ResourceLocation.tryParse(tag.getString(GENE_TAG))
					?: throw IllegalArgumentException("Invalid gene ResourceLocation in TemporaryGene NBT!")

				val rk = ResourceKey.create(ModGenes.GENE_REGISTRY_KEY, geneLocation)
				val geneHolder = registry.get(rk).getOrNull()
					?: throw IllegalArgumentException("Could not find gene with ResourceKey $rk when loading TemporaryGene!")

				val ticksRemaining = tag.getInt(TICKS_REMAINING_TAG)

				return TemporaryGene(geneHolder, ticksRemaining)
			}
		}
	}

}