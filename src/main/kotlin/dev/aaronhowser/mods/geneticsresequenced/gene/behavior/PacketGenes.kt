package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.projectile.DragonFireball
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3i
import java.util.*

object PacketGenes {

    private val recentTeleports = mutableSetOf<UUID>()

    @Suppress("MoveVariableDeclarationIntoWhen")
    fun teleport(player: ServerPlayer) {
        if (!ModGenes.teleport.isActive) return

        if (!player.hasGene(ModGenes.teleport)) return

        if (recentTeleports.contains(player.uuid)) return
        recentTeleports.add(player.uuid)
        ModScheduler.scheduleTaskInTicks(ServerConfig.teleportCooldown.get()) {
            recentTeleports.remove(player.uuid)


            val message = Component.empty()
                .append(ModGenes.teleport.nameComponent)
                .append(Component.translatable("cooldown.geneticsresequenced.ended"))

            player.sendSystemMessage(message)
        }

        val teleportDestination = player.lookAngle.normalize().scale(ServerConfig.teleportDistance.get())

        val lookingAtBlock: BlockHitResult = player.level().clip(
            ClipContext(
                player.eyePosition,
                player.eyePosition.add(teleportDestination),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
            )
        )

        var destination = if (lookingAtBlock.type == HitResult.Type.MISS) {
            player.eyePosition.add(teleportDestination)
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

        val blockAtNewFootLocation = player
            .level()
            .getBlockState(
                BlockPos(destination.toVec3i().offset(0, -1, 0))
            )
        val footBlockIsSolid = blockAtNewFootLocation.entityCanStandOn(
            player.level(),
            BlockPos(destination.toVec3i()),
            player
        )

        if (footBlockIsSolid) destination = destination.add(0.0, 1.0, 0.0)

        player.teleportTo(destination.x, destination.y, destination.z)
    }

    private val recentFireballs = GeneCooldown(
        ModGenes.shootFireballs,
        ServerConfig.dragonsBreathCooldown.get()
    )

    fun dragonBreath(player: ServerPlayer) {
        if (!ModGenes.dragonsBreath.isActive) return

        if (!player.hasGene(ModGenes.dragonsBreath)) return

        val wasNotOnCooldown = recentFireballs.add(player)

        if (!wasNotOnCooldown) return

        val entityDragonFireball = DragonFireball(
            player.level(),
            player,
            player.lookAngle
        ).apply {
            setPos(player.eyePosition)
        }

        player.level().addFreshEntity(entityDragonFireball)
    }

}