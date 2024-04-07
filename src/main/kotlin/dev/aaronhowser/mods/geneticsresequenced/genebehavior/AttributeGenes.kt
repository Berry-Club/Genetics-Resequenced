package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.attribute.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.SetEfficiencyPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.SetWallClimbingPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.player.Player
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.event.entity.player.PlayerEvent

object AttributeGenes {

    fun setEfficiency(player: Player, newLevel: Int) {
        val attributes = player.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
        attributes.baseValue = newLevel.toDouble()

        ModPacketHandler.messagePlayer(player as ServerPlayer, SetEfficiencyPacket(newLevel))
    }

    fun handleEfficiency(event: PlayerEvent.BreakSpeed) {
        val efficiencyAttribute = event.entity.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
        if (efficiencyAttribute.baseValue <= 0.0) return

        event.newSpeed += (1 + efficiencyAttribute.baseValue * efficiencyAttribute.baseValue).toFloat()
    }

    private val stepAssistAttributeModifier = AttributeModifier(
        "Genetics Resequenced: Step Assist",
        1.0,
        AttributeModifier.Operation.ADDITION
    )

    fun setStepAssist(player: Player, value: Boolean) {
        val stepHeightAttribute = player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()) ?: return

        if (value) {
            stepHeightAttribute.addPermanentModifier(stepAssistAttributeModifier)
            return
        } else {
            stepHeightAttribute.removeModifier(stepAssistAttributeModifier)
        }
    }

    fun setWallClimbing(player: Player, value: Boolean) {
        val attributes = player.attributes.getInstance(ModAttributes.WALL_CLIMBING) ?: return
        attributes.baseValue = if (value) 1.0 else 0.0

        ModPacketHandler.messagePlayer(player as ServerPlayer, SetWallClimbingPacket(value))
    }

    fun handleWallClimbing(player: Player) {
        if (!player.level.isClientSide) return

        val wallClimbingAttribute = player.attributes.getInstance(ModAttributes.WALL_CLIMBING) ?: return
        if (wallClimbingAttribute.baseValue <= 0.0) return

        val genes = player.getGenes() ?: return
        if (!genes.hasGene(Gene.WALL_CLIMBING)) return

        // ONLY HAPPENS ON CLIENT?????
        if (player.horizontalCollision || player.minorHorizontalCollision) {
            println(1)
            player.setDeltaMovement(player.deltaMovement.x, 0.3, player.deltaMovement.z)
            player.fallDistance = 0.0f
        }

    }

}