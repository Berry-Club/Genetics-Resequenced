package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import dev.aaronhowser.mods.geneticsresequenced.attribute.ModAttributes
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.player.PlayerEvent

object AttributeGenes {

    fun setEfficiency(player: Player, newLevel: Int) {

        if (player.level.isClientSide) {
            println("Set on client")
        } else {
            println("Set on server")
        }

        val attributes = player.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
        attributes.baseValue = newLevel.toDouble()
    }

    fun handleEfficiency(event: PlayerEvent.BreakSpeed) {
        val efficiencyAttribute = event.entity.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
        if (efficiencyAttribute.baseValue <= 0.0) return

        event.newSpeed += (1 + efficiencyAttribute.baseValue * efficiencyAttribute.baseValue).toFloat()
    }

}