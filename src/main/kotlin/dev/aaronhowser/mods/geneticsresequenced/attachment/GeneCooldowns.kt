package dev.aaronhowser.mods.geneticsresequenced.attachment

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.status
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.event.custom.GeneCooldownEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS

class GeneCooldowns(
	val cooldowns: List<Entry>
) {

	constructor() : this(emptyList())

	companion object {
		val MAP_CODEC: MapCodec<GeneCooldowns> =
			Entry.CODEC.listOf()
				.fieldOf("cooldowns")
				.xmap(::GeneCooldowns, GeneCooldowns::cooldowns)
		val CODEC: Codec<GeneCooldowns> = MAP_CODEC.codec()

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GeneCooldowns> =
			Entry.STREAM_CODEC.apply(ByteBufCodecs.list())
				.map(::GeneCooldowns, GeneCooldowns::cooldowns)

		var LivingEntity.geneCooldowns: GeneCooldowns
			get() = this.getData(ModAttachmentTypes.GENE_COOLDOWNS)
			set(value) {
				this.setData(ModAttachmentTypes.GENE_COOLDOWNS, value)
			}

		fun isOnCooldown(entity: LivingEntity, gene: Holder<Gene>): Boolean {
			return entity.geneCooldowns
				.cooldowns
				.any { it.geneHolder == gene }
		}

		fun isOnCooldown(entity: LivingEntity, geneRk: ResourceKey<Gene>): Boolean {
			val geneHolder = geneRk.getHolderOrThrow(entity.registryAccess())
			return isOnCooldown(entity, geneHolder)
		}

		fun addCooldown(
			entity: LivingEntity,
			geneRk: ResourceKey<Gene>,
			duration: Int,
			notify: Boolean = true
		): Boolean {
			val geneHolder = geneRk.getHolderOrThrow(entity.registryAccess())
			return addCooldown(entity, geneHolder, duration, notify)
		}

		fun addCooldown(
			entity: LivingEntity,
			gene: Holder<Gene>,
			duration: Int,
			notify: Boolean = true
		): Boolean {
			if (entity.isClientSide) return false
			if (isOnCooldown(entity, gene)) return false

			val event = GeneCooldownEvent.Add(entity, gene, duration)
			if (FORGE_BUS.post(event).isCanceled) {
				return false
			}

			val newEntry = Entry(gene, duration, notify)
			newEntry.notifyStart(entity)

			val newCooldowns = entity.geneCooldowns.cooldowns + newEntry
			entity.geneCooldowns = GeneCooldowns(newCooldowns)

			return true
		}

		fun removeCooldown(
			entity: LivingEntity,
			gene: Holder<Gene>
		): Boolean {
			if (entity.isClientSide) return false

			val cooldowns = entity.geneCooldowns.cooldowns
			val entry = cooldowns.firstOrNull { it.geneHolder == gene } ?: return false

			entry.notifyEnd(entity)

			val newCooldowns = cooldowns - entry
			entity.geneCooldowns = GeneCooldowns(newCooldowns)

			val event = GeneCooldownEvent.Remove(entity, gene)
			FORGE_BUS.post(event)

			return true
		}

		fun removeCooldown(
			entity: LivingEntity,
			geneRk: ResourceKey<Gene>
		): Boolean {
			val geneHolder = geneRk.getHolderOrThrow(entity.registryAccess())
			return removeCooldown(entity, geneHolder)
		}

		fun tick(entity: LivingEntity) {
			if (entity.isClientSide) return

			val cooldowns = entity.geneCooldowns.cooldowns
			val completed = mutableListOf<Entry>()
			for (entry in cooldowns) {
				entry.tick()

				if (entry.isCompleted()) {
					completed.add(entry)
					entry.notifyEnd(entity)

					val event = GeneCooldownEvent.Remove(entity, entry.geneHolder)
					FORGE_BUS.post(event)
				}
			}

			if (completed.isNotEmpty()) {
				val newCooldowns = cooldowns - completed.toSet()
				entity.geneCooldowns = GeneCooldowns(newCooldowns)
			}

		}
	}

	class Entry(
		val geneHolder: Holder<Gene>,
		val duration: Int,
		notify: Boolean = true
	) {
		val actuallyNotify = notify && duration >= ServerConfig.CONFIG.minimumCooldownForNotification.get()

		// Not persistent but like whatever, cooldowns probably won't ever be THAT long
		private var ticks = 0

		fun tick() {
			if (ticks < duration) {
				ticks++
			}
		}

		fun isCompleted(): Boolean = ticks >= duration
		fun remainingTicks(): Int = duration - ticks

		fun notifyStart(entity: LivingEntity) {
			if (!actuallyNotify || entity !is Player) return

			val totalSeconds = duration / 20

			val message = ModMessageLang.COOLDOWN_STARTED.toComponent(geneHolder.getName(), totalSeconds)
			entity.status(message)
		}

		fun notifyEnd(entity: LivingEntity) {
			if (!actuallyNotify || entity !is Player) return

			val message = ModMessageLang.COOLDOWN_ENDED.toComponent(geneHolder.getName())
			entity.status(message)
		}

		companion object {
			val CODEC: Codec<Entry> =
				RecordCodecBuilder.create { instance ->
					instance.group(
						Gene.CODEC
							.fieldOf("gene")
							.forGetter(Entry::geneHolder),
						Codec.INT
							.fieldOf("duration")
							.forGetter(Entry::duration),
						Codec.BOOL
							.optionalFieldOf("notify", true)
							.forGetter(Entry::actuallyNotify)
					).apply(instance, ::Entry)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Entry> =
				StreamCodec.composite(
					Gene.STREAM_CODEC, Entry::geneHolder,
					ByteBufCodecs.INT, Entry::duration,
					ByteBufCodecs.BOOL, Entry::actuallyNotify,
					::Entry
				)
		}
	}

}
