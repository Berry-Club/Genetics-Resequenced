package dev.aaronhowser.mods.geneticsresequenced.gene

import dev.aaronhowser.mods.aaron.AaronExtensions.registryAccess
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.LivingEntity
import java.util.*

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

		addedViaEntity = true

		val success = add(entity.uuid)

		if (success) {
			onAddSucceed(entity)
		} else {
			onAddFail(entity)
		}

		return success
	}

	private fun onAddSucceed(entity: LivingEntity) {
		if (this.actuallyNotify) tellCooldownStarted(entity, this.gene, this.cooldownTicks)

		entity.level().scheduleTaskInTicks(this.cooldownTicks) {
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

		return remove(entity.uuid)
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

			player.sendSystemMessage(message)
		}

		fun tellCooldownEnded(player: LivingEntity, geneRk: ResourceKey<Gene>) {
			val geneHolder = ModGenes.fromResourceLocation(player.registryAccess(), geneRk.location())!!
			val message =
				ModLanguageProvider.Cooldown.ENDED
					.toComponent(geneHolder.getName())

			player.sendSystemMessage(message)
		}

		fun tellOnCooldown(player: LivingEntity, geneRk: ResourceKey<Gene>) {
			val geneHolder = ModGenes.fromResourceLocation(player.registryAccess(), geneRk.location())!!
			val message = ModLanguageProvider.Cooldown.ON_COOLDOWN
				.toComponent(geneHolder.getName())

			player.sendSystemMessage(message)
		}
	}

}