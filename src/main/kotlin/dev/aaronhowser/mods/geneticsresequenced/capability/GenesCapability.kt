package dev.aaronhowser.mods.geneticsresequenced.capability

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.AaronExtensions.getLocationOrNull
import dev.aaronhowser.mods.aaron.AaronExtensions.isHolder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.TemporaryGenesData.Companion.temporaryGeneHolders
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneChangeEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isHelixOnly
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.SetGenesPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.jvm.optionals.getOrNull

class GenesCapability() {

	constructor(genes: Set<Holder<Gene>>) : this() {
		this.genes = genes
	}

	private var genes: Set<Holder<Gene>> = setOf()

	fun toTag(registries: HolderLookup.Provider): CompoundTag {
		val tag = CompoundTag()

		val listTag = ListTag()
		for (gene in genes) {
			val geneLocation = gene.getLocationOrNull() ?: continue
			val stringTag = StringTag.valueOf(geneLocation.toString())
			listTag.add(stringTag)
		}

		tag.put(GENES_TAG, listTag)
		return tag
	}

	fun fromTag(registries: HolderLookup.Provider, tag: CompoundTag) {
		val registry = registries.lookupOrThrow(ModGenes.GENE_REGISTRY_KEY)
		val listTag = tag.getList(GENES_TAG, Tag.TAG_STRING.toInt())

		val newGenes = mutableSetOf<Holder<Gene>>()

		for (i in listTag.indices) {
			val string = listTag.getString(i)
			val rl = ResourceLocation.tryParse(string) ?: continue
			val rk = ResourceKey.create(ModGenes.GENE_REGISTRY_KEY, rl)

			val geneHolder = registry.get(rk).getOrNull()
			if (geneHolder == null) {
				GeneticsResequenced.LOGGER.warn("Could not find gene with ResourceKey $rk when loading GenesCapability!")
				continue
			}

			newGenes += geneHolder
		}

		this.genes = newGenes
	}

	companion object {
		private const val GENES_TAG = "genes"

		val CODEC: Codec<GenesCapability> = Gene.CODEC.listOf().xmap(
			{ GenesCapability(it.toSet()) },
			{ it.genes.toList() }
		)

		fun syncPlayer(player: Player) {
			if (player !is ServerPlayer) return

			val packet = SetGenesPacket(player.id, player.permanentGeneHolders.mapNotNull { it.getLocationOrNull() })
			ModPacketHandler.messagePlayer(packet, player)
		}

		@JvmStatic
		var LivingEntity.permanentGeneHolders: Set<Holder<Gene>>
			get() {
				val cap = this.getCapability(GenesCapabilityProvider.CAPABILITY)
					.resolve()
					.getOrNull()

				if (cap == null) {
					GeneticsResequenced.LOGGER.warn("Tried to get genes capability for entity ${this.name.string} but it was null!")
					return emptySet()
				}

				return cap.genes
			}
			private set(value) {
				val cap = this.getCapability(GenesCapabilityProvider.CAPABILITY)
					.resolve()
					.getOrNull()

				if (cap == null) {
					GeneticsResequenced.LOGGER.warn("Tried to set genes capability for entity ${this.name.string} but it was null!")
					return
				}

				cap.genes = value
			}

		@JvmStatic
		fun LivingEntity.getGenes(): Set<Holder<Gene>> {
			return this.permanentGeneHolders + this.temporaryGeneHolders
		}

		@JvmStatic
		fun LivingEntity.addGene(newGeneHolder: Holder<Gene>): Boolean {
			if (this.hasGene(newGeneHolder)) return false

			if (newGeneHolder.isHelixOnly) {
				GeneticsResequenced.Companion.LOGGER.debug(
					"Cannot add gene $newGeneHolder to entities, as it has tag `#geneticsresequenced:helix_only`."
				)
				return false
			}

			if (
				this is Player
				&& newGeneHolder.isNegative
				&& ServerConfig.Companion.CONFIG.disableGivingPlayersNegativeGenes.get()
				&& !newGeneHolder.isHolder(ModGenes.CRINGE)
			) {
				GeneticsResequenced.Companion.LOGGER.debug(
					StringBuilder()
						.append("Tried to give negative gene ")
						.append(newGeneHolder.getLocationOrNull() ?: newGeneHolder)
						.append(" to player ").append(name.string)
						.append(", but \"disableGivingPlayersNegativeGenes\" is true in the server config.")
						.toString()
				)
				return false
			}

			val allowedTypes = newGeneHolder.value().allowedEntities.map(Holder<EntityType<*>>::value)
			if (this.type !in allowedTypes) {
				GeneticsResequenced.Companion.LOGGER.debug(
					StringBuilder()
						.append("Tried to give gene ")
						.append(newGeneHolder.getLocationOrNull() ?: newGeneHolder)
						.append(" to entity ").append(name.string)
						.append(", but that entity type cannot have that gene!")
						.toString()
				)
				return false
			}

			val incompatibleGenes = newGeneHolder.value().incompatibleGenes
			val foundIncompatibleGenes = this.permanentGeneHolders.filter { it.unwrapKey().getOrNull() in incompatibleGenes }
			if (foundIncompatibleGenes.isNotEmpty()) {
				GeneticsResequenced.Companion.LOGGER.debug(
					StringBuilder()
						.append("Tried to give gene ")
						.append(newGeneHolder.getLocationOrNull() ?: newGeneHolder)
						.append(" to entity ").append(name.string)
						.append(", but it is incompatible with the following genes the entity already has: ")
						.append(foundIncompatibleGenes.joinToString { it.getLocationOrNull().toString() })
						.toString()
				)
				return false
			}

			val eventPre = GeneChangeEvent.Pre(this@addGene, newGeneHolder, true)
			FORGE_BUS.post(eventPre)
			if (eventPre.isCanceled) {
				GeneticsResequenced.Companion.LOGGER.debug("Event was canceled: $eventPre")
				return false
			}

			this.permanentGeneHolders += newGeneHolder

			val eventPost = GeneChangeEvent.Post(this@addGene, newGeneHolder, true)
			FORGE_BUS.post(eventPost)

			return true
		}


		@JvmStatic
		fun LivingEntity.removeGene(removedGeneHolder: Holder<Gene>): Boolean {
			if (!this.hasGene(removedGeneHolder)) return false

			val eventPre = GeneChangeEvent.Pre(this, removedGeneHolder, false)
			FORGE_BUS.post(eventPre)
			if (eventPre.isCanceled) {
				GeneticsResequenced.Companion.LOGGER.debug("Event was canceled: $eventPre")
				return false
			}

			this.permanentGeneHolders -= removedGeneHolder

			val eventPost = GeneChangeEvent.Post(this, removedGeneHolder, false)
			FORGE_BUS.post(eventPost)

			return true
		}

		@JvmStatic
		@OptIn(ExperimentalContracts::class)
		fun Entity.hasPermanentGene(gene: Holder<Gene>): Boolean {
			contract { returns(true) implies (this@hasPermanentGene is LivingEntity) }
			if (gene.isDisabled || this !is LivingEntity) return false

			return gene in this.permanentGeneHolders
		}

		@JvmStatic
		@OptIn(ExperimentalContracts::class)
		fun Entity.hasGene(gene: Holder<Gene>): Boolean {
			contract { returns(true) implies (this@hasGene is LivingEntity) }
			if (gene.isDisabled || this !is LivingEntity) return false

			return gene in this.permanentGeneHolders || gene in this.temporaryGeneHolders
		}

		@JvmStatic
		@OptIn(ExperimentalContracts::class)
		fun Entity.hasGene(geneKey: ResourceKey<Gene>): Boolean {
			contract { returns(true) implies (this@hasGene is LivingEntity) }

			val holder = ModGenes.fromResourceKey(level().registryAccess(), geneKey) ?: return false
			return this.hasGene(holder)
		}

		@JvmStatic
		fun LivingEntity.removeAllGenes() {
			for (gene in this.permanentGeneHolders) {
				this.removeGene(gene)
			}
		}

		@JvmStatic
		fun LivingEntity.addAllGenes(registries: HolderLookup.Provider, includeNegative: Boolean = false) {
			val genesToAdd =
				ModGenes.getAllGeneHolders(registries).filter { includeNegative || !it.isNegative }

			for (gene in genesToAdd) {
				this.addGene(gene)
			}
		}

	}
}