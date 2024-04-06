package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.ClipBlockStateContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

object PacketGenes {

    fun teleport(player: ServerPlayer) {
        val lookDirection = player.lookAngle.normalize().scale(10.0)

        val lookingAtBlock: BlockHitResult = player.level.isBlockInLine(ClipBlockStateContext(
            player.eyePosition,
            player.eyePosition.add(lookDirection)
        ) { state -> !state.isAir })

        val destination = if (lookingAtBlock.type == HitResult.Type.MISS) {
            player.eyePosition.add(lookDirection)
        } else {

            val blockLocation = lookingAtBlock.location.add(0.5, 1.1, 0.5)
            val sideHit = lookingAtBlock.direction

            println("Block hit: $blockLocation")
            println("Side hit: $sideHit")

            val offset = when (sideHit) {
                Direction.UP -> Vec3(0.0, 1.0, 0.0)
                Direction.DOWN -> Vec3(0.0, -1.0, 0.0)
                Direction.NORTH -> Vec3(0.0, 0.0, -1.0)
                Direction.SOUTH -> Vec3(0.0, 0.0, 1.0)
                Direction.WEST -> Vec3(-1.0, 0.0, 0.0)
                Direction.EAST -> Vec3(1.0, 0.0, 0.0)
                else -> Vec3.ZERO
            }

            blockLocation.add(offset)
        }
        println("Destination: $destination")

        player.teleportTo(destination.x, destination.y, destination.z)
    }

}