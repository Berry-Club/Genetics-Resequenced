package dev.aaronhowser.mods.geneticsresequenced.gene

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
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
		return success
	}

	fun remove(entity: LivingEntity): Boolean {

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

}