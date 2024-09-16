package dev.aaronhowser.mods.geneticsresequenced.gene

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.LivingEntity
import java.util.*

class GeneCooldown(
    private val gene: ResourceKey<Gene>,
    private val cooldownTicks: Int,
    notifyPlayer: Boolean = true
) : MutableSet<UUID> {

    private val uuidsOnCooldown: MutableSet<UUID> = mutableSetOf()
    private val actuallyNotify = notifyPlayer && cooldownTicks >= ServerConfig.minimumCooldownForNotification.get()

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
        if (actuallyNotify) tellCooldownStarted(entity, gene, cooldownTicks)

        ModScheduler.scheduleTaskInTicks(cooldownTicks) {
            remove(entity)
        }
    }

    private fun onAddFail(entity: LivingEntity) {
        if (actuallyNotify) tellOnCooldown(entity, gene)
    }

    fun remove(entity: LivingEntity): Boolean {
        if (entity.uuid in this) {
            if (actuallyNotify) tellCooldownEnded(entity, gene)
        }

        return remove(entity.uuid)
    }

    override fun add(element: UUID): Boolean {
        if (!addedViaEntity) throw UnsupportedOperationException(
            "Cannot add UUIDs directly to GeneCooldown"
        )

        if (element in uuidsOnCooldown) return false

        return uuidsOnCooldown.add(element)
    }

    override fun remove(element: UUID): Boolean {
        if (cooldownEndedTasks.isNotEmpty()) {
            for (task in cooldownEndedTasks) {
                task()
            }
            GeneticsResequenced.LOGGER.debug("$this ran ${cooldownEndedTasks.size} tasks as it ended")
        }

        cooldownEndedTasks.clear()

        return uuidsOnCooldown.remove(element)
    }

    override val size: Int = uuidsOnCooldown.size

    override fun clear() = uuidsOnCooldown.clear()

    override fun isEmpty(): Boolean = uuidsOnCooldown.isEmpty()

    override fun iterator(): MutableIterator<UUID> = uuidsOnCooldown.iterator()

    override fun retainAll(elements: Collection<UUID>): Boolean = uuidsOnCooldown.retainAll(elements.toSet())

    override fun removeAll(elements: Collection<UUID>): Boolean = uuidsOnCooldown.removeAll(elements.toSet())

    override fun containsAll(elements: Collection<UUID>): Boolean = uuidsOnCooldown.containsAll(elements)

    override fun contains(element: UUID): Boolean = uuidsOnCooldown.contains(element)

    override fun addAll(elements: Collection<UUID>): Boolean = uuidsOnCooldown.addAll(elements)

    override fun toString(): String = "GeneCooldown($gene)"

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

            val gene = GeneRegistry.fromResourceLocation()

            val message = Component.empty()
                .append(geneRk.nameComponent)
                .append(ModLanguageProvider.Cooldown.STARTED.toComponent(cooldownString))

            player.sendSystemMessage(message)
        }

        fun tellCooldownEnded(player: LivingEntity, gene: Gene) {
            val message = ModLanguageProvider.Cooldown.ENDED.toComponent(gene.nameComponent)

            player.sendSystemMessage(message)
        }

        fun tellOnCooldown(player: LivingEntity, gene: Gene) {
            val message = ModLanguageProvider.Cooldown.ON_COOLDOWN.toComponent(gene.nameComponent)

            player.sendSystemMessage(message)
        }
    }

}