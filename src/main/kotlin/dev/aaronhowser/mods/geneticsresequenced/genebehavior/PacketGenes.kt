package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.ClipBlockStateContext
import net.minecraft.world.phys.HitResult

object PacketGenes {

    fun teleport(player: ServerPlayer) {
        val lookDirection = player.lookAngle.normalize().scale(5.0)

        val blockInPath = player.level.isBlockInLine(ClipBlockStateContext(
            player.eyePosition,
            player.eyePosition.add(lookDirection)
        ) { state -> !state.isAir }).type == HitResult.Type.MISS

        println("Block in path: $blockInPath")
    }

}