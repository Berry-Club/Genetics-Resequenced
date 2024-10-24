package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.gene.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.gene.BaseModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.BaseModGenes.getHolder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.projectile.DragonFireball
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3i

object PacketGenes {

    private val recentTeleports = GeneCooldown(
        BaseModGenes.TELEPORT,
        ServerConfig.teleportCooldown.get()
    )

    @Suppress("MoveVariableDeclarationIntoWhen")
    fun teleport(player: ServerPlayer) {
        val teleport = BaseModGenes.TELEPORT.getHolder(player.registryAccess()) ?: return
        if (teleport.isDisabled) return

        if (!player.hasGene(BaseModGenes.TELEPORT)) return

        val wasNotOnCooldown = recentTeleports.add(player)
        if (!wasNotOnCooldown) return

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

    private val recentDragonsBreath = GeneCooldown(
        BaseModGenes.DRAGON_BREATH,
        ServerConfig.dragonsBreathCooldown.get()
    )

    fun dragonBreath(player: ServerPlayer) {
        val dragonBreath = BaseModGenes.DRAGON_BREATH.getHolder(player.registryAccess()) ?: return
        if (dragonBreath.isDisabled) return

        if (!player.hasGene(BaseModGenes.DRAGON_BREATH)) return

        val wasNotOnCooldown = recentDragonsBreath.add(player)

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