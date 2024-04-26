package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import java.util.*

object OtherUtil {

    /**
     * @param notifyPlayer If the player should be notified when the cooldown starts and ends. Preferably, only passive ones (like Lay Eggs and Meaty 2) should be false.
     * @return true if the player was added to the cooldown, false if they were already on it
     */
    fun tryAddToCooldown(
        cooldownMap: MutableSet<UUID>,
        player: LivingEntity,
        gene: Gene,
        cooldownTicks: Int,
        notifyPlayer: Boolean = true
    ): Boolean {
        if (cooldownMap.contains(player.uuid)) return false

        val actuallyNotifyPlayer = if (notifyPlayer) {
            cooldownTicks >= ServerConfig.minimumCooldownForNotification.get()
        } else false

        cooldownMap.add(player.uuid)
        ModScheduler.scheduleTaskInTicks(cooldownTicks) {
            cooldownMap.remove(player.uuid)
            if (actuallyNotifyPlayer) tellCooldownEnded(player, gene)
        }

        if (actuallyNotifyPlayer) tellCooldownStarted(player, gene, cooldownTicks)
        return true
    }

    private fun tellCooldownStarted(player: LivingEntity, gene: Gene, cooldownTicks: Int) {
        val cooldownSeconds = cooldownTicks / 20
        val cooldownString: String
        if (cooldownSeconds > 60) {
            val minutes = cooldownSeconds / 60
            val seconds = cooldownSeconds % 60
            cooldownString = "$minutes minutes and $seconds seconds"
        } else {
            cooldownString = "$cooldownSeconds seconds"
        }

        val message = Component.empty()
            .append(gene.nameComponent)
            .append(Component.translatable("cooldown.geneticsresequenced.started", cooldownString))

        player.sendSystemMessage(message)
    }

    private fun tellCooldownEnded(player: LivingEntity, gene: Gene) {
        val message = Component.empty()
            .append(gene.nameComponent)
            .append(Component.translatable("cooldown.geneticsresequenced.ended"))

        player.sendSystemMessage(message)
    }

    fun modResource(path: String): ResourceLocation = ResourceLocation(GeneticsResequenced.ID, path)

}