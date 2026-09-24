package dev.aaronhowser.mods.genetics_resequenced.gene

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.registryAccess
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.event.custom.GeneCooldownEvent
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import java.util.*
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

class GeneCooldown(
	private val gene: ResourceKey<Gene>,
	private val cooldownTicks: Int, // TODO: Replace this with a getter, so you can configure it mid-game
	notifyPlayer: Boolean = true
) : MutableSet<UUID> {

	private val uuidsOnCooldown: MutableSet<UUID> = mutableSetOf()
	private val actuallyNotify = notifyPlayer && cooldownTicks >= ServerConfig.CONFIG.minimumCooldownForNotification.get()

	private var addedViaEntity = false

	val cooldownEndedTasks: MutableSet<() -> Unit> = mutableSetOf()

	fun add(entity: LivingEntity): Boolean {
		val geneHolder = ModGenes.fromResourceLocation(entity.registryAccess(), gene.location()) ?: return false
		val event = GeneCooldownEvent.Add(entity, geneHolder, cooldownTicks)
		if (FORGE_BUS.post(event)) return false

		addedViaEntity = true

		val success = add(entity.uuid)

		if (success) {
			onAddSucceed(entity, event.cooldownTicks)
		} else {
			onAddFail(entity)
		}

		return success
	}

	private fun onAddSucceed(entity: LivingEntity, duration: Int) {
		if (this.actuallyNotify) tellCooldownStarted(entity, this.gene, duration)

		entity.level().scheduleTaskInTicks(duration) {
			remove(entity)
		}
	}

	private fun onAddFail(entity: LivingEntity) {
		if (this.actuallyNotify) tellOnCooldown(entity, this.gene)
	}

	fun remove(entity: LivingEntity): Boolean {
		if (entity.uuid in this) {
			if (this.actuallyNotify) tellCooldownEnded(entity, this.gene)
		}

		val removed = remove(entity.uuid)
		if (removed) {
			val geneHolder = ModGenes.fromResourceLocation(entity.registryAccess(), gene.location())
			if (geneHolder != null) {
				FORGE_BUS.post(GeneCooldownEvent.Remove(entity, geneHolder))
			}
		}

		return removed
	}

	override fun add(element: UUID): Boolean {
		if (!this.addedViaEntity) throw UnsupportedOperationException(
			"Cannot add UUIDs directly to GeneCooldown"
		)

		if (element in this.uuidsOnCooldown) return false

		return this.uuidsOnCooldown.add(element)
	}

	override fun remove(element: UUID): Boolean {
		if (this.cooldownEndedTasks.isNotEmpty()) {
			for (task in this.cooldownEndedTasks) {
				task()
			}
			GeneticsResequenced.LOGGER.debug("$this ran ${this.cooldownEndedTasks.size} tasks as it ended")
		}

		this.cooldownEndedTasks.clear()

		return this.uuidsOnCooldown.remove(element)
	}

	// TODO: WHy the hell is all this here? Why are we extending MutableSet?
	override val size: Int = this.uuidsOnCooldown.size
	override fun clear() = this.uuidsOnCooldown.clear()
	override fun isEmpty(): Boolean = this.uuidsOnCooldown.isEmpty()
	override fun iterator(): MutableIterator<UUID> = this.uuidsOnCooldown.iterator()
	override fun retainAll(elements: Collection<UUID>): Boolean = this.uuidsOnCooldown.retainAll(elements.toSet())
	override fun removeAll(elements: Collection<UUID>): Boolean = this.uuidsOnCooldown.removeAll(elements.toSet())
	override fun containsAll(elements: Collection<UUID>): Boolean = this.uuidsOnCooldown.containsAll(elements)
	override fun contains(element: UUID): Boolean = this.uuidsOnCooldown.contains(element)
	override fun addAll(elements: Collection<UUID>): Boolean = this.uuidsOnCooldown.addAll(elements)
	override fun toString(): String = "GeneCooldown(${this.gene})"

	companion object {
		fun tellCooldownStarted(player: LivingEntity, geneRk: ResourceKey<Gene>, cooldownTicks: Int) {
			val cooldownSeconds = cooldownTicks / 20
			val cooldownString: String
			if (cooldownSeconds > 60) {
				val minutes = cooldownSeconds / 60
				val seconds = cooldownSeconds % 60
				cooldownString = "$minutes minutes and $seconds seconds"
			} else {
				cooldownString = "$cooldownSeconds seconds"
			}

			val geneHolder = ModGenes.fromResourceLocation(player.registryAccess(), geneRk.location())!!

			val message = Component.empty()
				.append(geneHolder.getName())
				.append(ModLanguageProvider.Cooldown.STARTED.toComponent(cooldownString))

			if (player is Player) player.displayClientMessage(message, true)
		}

		fun tellCooldownEnded(player: LivingEntity, geneRk: ResourceKey<Gene>) {
			val geneHolder = ModGenes.fromResourceLocation(player.registryAccess(), geneRk.location())!!
			val message =
				ModLanguageProvider.Cooldown.ENDED
					.toComponent(geneHolder.getName())

			if (player is Player) player.displayClientMessage(message, true)
		}

		fun tellOnCooldown(player: LivingEntity, geneRk: ResourceKey<Gene>) {
			val geneHolder = ModGenes.fromResourceLocation(player.registryAccess(), geneRk.location())!!
			val message = ModLanguageProvider.Cooldown.ON_COOLDOWN
				.toComponent(geneHolder.getName())

			if (player is Player) player.displayClientMessage(message, true)
		}
	}

}