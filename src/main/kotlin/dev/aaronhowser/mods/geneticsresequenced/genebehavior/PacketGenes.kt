package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.forge.vectorutil.toVec3

object PacketGenes {

    @Suppress("MoveVariableDeclarationIntoWhen")
    fun teleport(player: ServerPlayer) {
        val lookDirection = player.lookAngle.normalize().scale(10.0)

        val lookingAtBlock: BlockHitResult = player.level.clip(
            ClipContext(
                player.eyePosition,
                player.eyePosition.add(lookDirection),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
            )
        )

        var destination = if (lookingAtBlock.type == HitResult.Type.MISS) {
            player.eyePosition.add(lookDirection)
        } else {

            val blockLocation = lookingAtBlock.location
            val sideHit = lookingAtBlock.direction

            val offset = when (sideHit) {
                Direction.DOWN -> Vec3(0.0, -2.0, 0.0)
                Direction.UP -> Vec3(0.0, 0.5, 0.0)
                else -> sideHit.normal.toVec3()
            }

            blockLocation.add(offset)
        }

        val blockAtNewFootLocation = player.level.getBlockState(BlockPos(destination.add(0.0, -1.0, 0.0)))
        val footBlockIsSolid = blockAtNewFootLocation.entityCanStandOn(player.level, BlockPos(destination), player)

        if (footBlockIsSolid) destination = destination.add(0.0, 1.0, 0.0)

        player.teleportTo(destination.x, destination.y, destination.z)
    }

}