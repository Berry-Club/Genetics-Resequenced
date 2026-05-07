package dev.aaronhowser.mods.geneticsresequenced.attachment

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.player.Player

class GeneCooldowns(
	val cooldowns: List<Entry>
) {

	constructor() : this(emptyList())

	companion object {
		val CODEC: Codec<GeneCooldowns> =
			Entry.CODEC.listOf()
				.xmap(::GeneCooldowns, GeneCooldowns::cooldowns)

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GeneCooldowns> =
			Entry.STREAM_CODEC.apply(ByteBufCodecs.list())
				.map(::GeneCooldowns, GeneCooldowns::cooldowns)

		var Player.geneCooldowns: GeneCooldowns
			get() = this.getData(ModAttachmentTypes.GENE_COOLDOWNS)
			set(value) {
				this.setData(ModAttachmentTypes.GENE_COOLDOWNS, value)
			}

		fun tick(player: Player) {
			val cooldowns = player.geneCooldowns.cooldowns

			cooldowns.forEach(Entry::tick)
			val completed = cooldowns.filter(Entry::isCompleted)

			if (completed.isNotEmpty()) {
				val newCooldowns = cooldowns - completed.toSet()
				player.geneCooldowns = GeneCooldowns(newCooldowns)
			}

		}
	}

	class Entry(
		val gene: Holder<Gene>,
		val cooldownDuration: Int,
		notifyPlayer: Boolean = true
	) {
		val actuallyNotify = notifyPlayer && cooldownDuration >= ServerConfig.CONFIG.minimumCooldownForNotification.get()

		// Not persistent but like whatever, cooldowns probably won't ever be THAT long
		private var ticks = 0

		fun tick() {
			if (ticks < cooldownDuration) {
				ticks++
			}
		}

		fun isCompleted(): Boolean = ticks >= cooldownDuration

		companion object {
			val CODEC: Codec<Entry> =
				RecordCodecBuilder.create { instance ->
					instance.group(
						Gene.CODEC
							.fieldOf("gene")
							.forGetter(Entry::gene),
						Codec.INT
							.fieldOf("duration")
							.forGetter(Entry::cooldownDuration),
						Codec.BOOL
							.optionalFieldOf("notify", true)
							.forGetter(Entry::actuallyNotify)
					).apply(instance, ::Entry)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, Entry> =
				StreamCodec.composite(
					Gene.STREAM_CODEC, Entry::gene,
					ByteBufCodecs.INT, Entry::cooldownDuration,
					ByteBufCodecs.BOOL, Entry::actuallyNotify,
					::Entry
				)
		}
	}

}