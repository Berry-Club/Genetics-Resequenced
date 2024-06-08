package dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.util.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.DragonFireball
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.forge.vectorutil.toVec3
import java.util.*

object PacketGenes {

    private val recentTeleports = mutableSetOf<UUID>()

    @Suppress("MoveVariableDeclarationIntoWhen")
    fun teleport(player: ServerPlayer) {
        if (!DefaultGenes.teleport.isActive) return

        val genes = (player as LivingEntity).getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.teleport)) return

        if (recentTeleports.contains(player.uuid)) return
        recentTeleports.add(player.uuid)
        ModScheduler.scheduleTaskInTicks(ServerConfig.teleportCooldown.get()) {
            recentTeleports.remove(player.uuid)


            val message = Component.empty()
                .append(DefaultGenes.teleport.nameComponent)
                .append(Component.translatable("cooldown.geneticsresequenced.ended"))

            player.sendSystemMessage(message)
        }

        val teleportDestination = player.lookAngle.normalize().scale(ServerConfig.teleportDistance.get())

        val lookingAtBlock: BlockHitResult = player.level.clip(
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

        val blockAtNewFootLocation = player.level.getBlockState(BlockPos(destination.add(0.0, -1.0, 0.0)))
        val footBlockIsSolid = blockAtNewFootLocation.entityCanStandOn(player.level, BlockPos(destination), player)

        if (footBlockIsSolid) destination = destination.add(0.0, 1.0, 0.0)

        player.teleportTo(destination.x, destination.y, destination.z)
    }

    private val recentFireballs = GeneCooldown(
        DefaultGenes.shootFireballs,
        ServerConfig.dragonsBreathCooldown.get()
    )

    fun dragonBreath(player: ServerPlayer) {
        if (!DefaultGenes.dragonsBreath.isActive) return

        val genes = (player as LivingEntity).getGenes() ?: return
        if (!genes.hasGene(DefaultGenes.dragonsBreath)) return

        val wasNotOnCooldown = recentFireballs.add(player)

        if (!wasNotOnCooldown) return

        val entityDragonFireball = DragonFireball(
            player.level,
            player,
            player.lookAngle.x,
            player.lookAngle.y,
            player.lookAngle.z
        ).apply {
            setPos(player.eyePosition)
        }

        player.level.addFreshEntity(entityDragonFireball)
    }

}