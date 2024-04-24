package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity

object OtherUtil {

    fun tellCooldownEnded(player: LivingEntity, gene: Gene) {
        val message = Component.empty()
            .append(gene.nameComponent)
            .append(Component.translatable("cooldown.geneticsresequenced.ended"))

        player.sendSystemMessage(message)
    }

}