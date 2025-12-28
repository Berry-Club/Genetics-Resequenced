package dev.aaronhowser.mods.geneticsresequenced.attachment

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.TemporaryGeneAddedEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.TemporaryGeneRemovedEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isHelixOnly
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

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

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TemporaryGenesData> =
			StreamCodec.composite(
				TemporaryGene.STREAM_CODEC.apply(ByteBufCodecs.list()),
				TemporaryGenesData::temporaryGenes,
				::TemporaryGenesData
			)

		@JvmStatic
		var LivingEntity.temporaryGenes: List<TemporaryGene>
			get() = this.getData(ModAttachmentTypes.TEMPORARY_GENES).temporaryGenes
			private set(value) {
				this.setData(ModAttachmentTypes.TEMPORARY_GENES, TemporaryGenesData(value.toMutableList()))
			}

		@JvmStatic
		val LivingEntity.temporaryGeneHolders: List<Holder<Gene>>
			get() = this.temporaryGenes.map(TemporaryGene::geneHolder)

		fun tickTemporaryGenes(entity: LivingEntity) {
			val copy = entity.temporaryGenes.toList()
			for (tempGene in copy) {
				if (tempGene.tick()) {
					entity.removeTemporaryGene(tempGene.geneHolder)
				}
			}
		}

		fun LivingEntity.removeTemporaryGene(
			geneHolderToRemove: Holder<Gene>
		) {
			val existingList = this.temporaryGenes.toMutableList()
			val wasRemoved = existingList.removeIf { it.geneHolder.isGene(geneHolderToRemove) }
			if (!wasRemoved) return

			if (geneHolderToRemove.value().potions.isNotEmpty()) {
				TickGenes.handlePotionGeneRemoved(this, geneHolderToRemove)
			}

			val event = TemporaryGeneRemovedEvent(this, geneHolderToRemove)
			FORGE_BUS.post(event)

			this.temporaryGenes = existingList
		}

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

			val foundIncompatibleGenes = this.getGenes().filter { it.key in newGeneHolder.value().incompatibleGenes }
			if (foundIncompatibleGenes.isNotEmpty()) {
				GeneticsResequenced.LOGGER.debug(
					StringBuilder()
						.append("Tried to give temporary gene ")
						.append(newGeneHolder.key?.location() ?: newGeneHolder)
						.append(" to entity ").append(name.string)
						.append(", but it is incompatible with the following genes the entity already has: ")
						.append(foundIncompatibleGenes.joinToString { it.key?.location().toString() })
						.toString()
				)
				return false
			}

			val eventPre = TemporaryGeneAddedEvent.Pre(this, newGeneHolder, durationTicks)
			if (FORGE_BUS.post(eventPre).isCanceled) {
				GeneticsResequenced.LOGGER.debug("Event was canceled: $eventPre")
				return false
			}

			val existingList = this.temporaryGenes.toMutableList()

			val existingTempGene = existingList.find { it.geneHolder.isGene(newGeneHolder) }
			if (existingTempGene != null) {
				existingTempGene.ticksRemaining = durationTicks
			} else {
				existingList.add(TemporaryGene(newGeneHolder, durationTicks))
			}

			this.temporaryGenes = existingList

			val eventPost = TemporaryGeneAddedEvent.Post(this, newGeneHolder, durationTicks)
			FORGE_BUS.post(eventPost)

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

		fun getComponent(): Component {
			return ModLanguageProvider.Commands.TEMPORARY_GENE_WITH_DURATION.toComponent(
				geneHolder.getName(),
				ticksRemaining
			)
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

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TemporaryGene> =
				StreamCodec.composite(
					Gene.STREAM_CODEC, TemporaryGene::geneHolder,
					ByteBufCodecs.VAR_INT, TemporaryGene::ticksRemaining,
					::TemporaryGene
				)
		}
	}

}