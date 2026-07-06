package dev.aaronhowser.mods.geneticsresequenced.attachment

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.getActiveGenes
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.event.custom.TemporaryGeneAddedEvent
import dev.aaronhowser.mods.geneticsresequenced.event.custom.TemporaryGeneRemovedEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isHelixOnly
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
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
			val toRemove = mutableListOf<TemporaryGene>()
			val tempGenes = entity.temporaryGenes

			for (tempGene in tempGenes) {
				if (tempGene.tick()) {
					toRemove.add(tempGene)
				}
			}

			for (tempGene in toRemove) {
				entity.removeTemporaryGene(tempGene.geneHolder)
			}
		}

		@JvmStatic
		fun LivingEntity.removeTemporaryGene(
			geneRk: ResourceKey<Gene>
		): Boolean {
			val geneHolder = ModGenes.fromResourceKey(registryAccess(), geneRk) ?: return false
			return removeTemporaryGene(geneHolder)
		}

		@JvmStatic
		fun LivingEntity.removeTemporaryGene(
			geneHolder: Holder<Gene>
		): Boolean {
			val existingList = this.temporaryGenes.toMutableList()
			val wasRemoved = existingList.removeIf { it.geneHolder.isGene(geneHolder) }
			if (!wasRemoved) return false

			val eventPre = TemporaryGeneRemovedEvent.Pre(this, geneHolder)
			if (FORGE_BUS.post(eventPre).isCanceled) {
				GeneticsResequenced.LOGGER.debug("Event was canceled: $eventPre")
				return false
			}

			if (geneHolder.value().potions.isNotEmpty()) {
				TickGenes.handlePotionGeneRemoved(this, geneHolder)
			}

			this.temporaryGenes = existingList

			val eventPost = TemporaryGeneRemovedEvent.Post(this, geneHolder)
			FORGE_BUS.post(eventPost)

			return true
		}

		@JvmStatic
		fun LivingEntity.addTemporaryGene(
			newGeneRk: ResourceKey<Gene>,
			durationTicks: Int
		): Boolean {
			val geneHolder = ModGenes.fromResourceKey(registryAccess(), newGeneRk) ?: return false
			return addTemporaryGene(geneHolder, durationTicks)
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

			val foundIncompatibleGenes = this.getActiveGenes().filter { it.key in newGeneHolder.value().incompatibleGenes }
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
			val finalDurationTicks = eventPre.durationTicks

			val existingList = this.temporaryGenes.toMutableList()

			val existingTempGene = existingList.find { it.geneHolder.isGene(newGeneHolder) }
			if (existingTempGene != null) {
				existingTempGene.ticksRemaining = finalDurationTicks
			} else {
				existingList.add(TemporaryGene(newGeneHolder, finalDurationTicks))
			}

			this.temporaryGenes = existingList

			val eventPost = TemporaryGeneAddedEvent.Post(this, newGeneHolder, finalDurationTicks)
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
			return ModMessageLang.TEMPORARY_GENE_WITH_DURATION.toComponent(
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
