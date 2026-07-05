package dev.aaronhowser.mods.genetics_resequenced.gene.behavior

import dev.aaronhowser.mods.genetics_resequenced.attachment.GeneCooldowns
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

object PacketGenes {

	@Suppress("MoveVariableDeclarationIntoWhen")
	fun teleport(player: ServerPlayer) {
		if (!player.hasGene(ModGenes.TELEPORT)) return

		val putOnCooldown = GeneCooldowns.addCooldown(
			player,
			ModGenes.TELEPORT,
			ServerConfig.CONFIG.teleportCooldown.get()
		)

		if (!putOnCooldown) return

		val teleportDestination = player.lookAngle.normalize().scale(ServerConfig.CONFIG.teleportDistance.get())

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
				else -> sideHit.unitVec3
			}

			blockLocation.add(offset)
		}

		val blockAtNewFootLocation = player
			.level()
			.getBlockState(BlockPos.containing(destination).below())
		val footBlockIsSolid = blockAtNewFootLocation.entityCanStandOn(
			player.level(),
			BlockPos.containing(destination),
			player
		)

		if (footBlockIsSolid) destination = destination.add(0.0, 1.0, 0.0)

		player.teleportTo(destination.x, destination.y, destination.z)
	}

	fun dragonBreath(player: ServerPlayer) {
		if (!player.hasGene(ModGenes.DRAGON_BREATH)) return

		val putOnCooldown = GeneCooldowns.addCooldown(
			player,
			ModGenes.DRAGON_BREATH,
			ServerConfig.CONFIG.dragonsBreathCooldown.get()
		)

		if (!putOnCooldown) return

		val entityDragonFireball = DragonFireball(
			player.level(),
			player,
			player.lookAngle
		)

		entityDragonFireball.setPos(player.eyePosition)

		player.level().addFreshEntity(entityDragonFireball)
	}

}
