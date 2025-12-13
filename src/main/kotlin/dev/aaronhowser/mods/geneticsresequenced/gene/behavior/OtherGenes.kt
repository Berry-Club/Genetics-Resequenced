package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.aaron.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.NarratorPacket
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Pose
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.event.ServerChatEvent
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent
import kotlin.random.Random

object OtherGenes {

	private val VILLAGER_SOUNDS = listOf(
		SoundEvents.VILLAGER_TRADE,
		SoundEvents.VILLAGER_AMBIENT,
		SoundEvents.VILLAGER_CELEBRATE
	)

	fun handleEmeraldHeart(event: ServerChatEvent) {
		val emeraldHeart = ModGenes.EMERALD_HEART.getHolderOrThrow(event.player.registryAccess())
		if (emeraldHeart.isDisabled) return

		val player = event.player
		if (!player.random.chance(ServerConfig.CONFIG.emeraldHeartChatChance.get())) return

		if (player.hasGene(ModGenes.EMERALD_HEART)) {
			player.level().playSound(
				null,
				player.blockPosition(),
				VILLAGER_SOUNDS.random(),
				player.soundSource,
				1f,
				1f
			)
		}
	}

	fun handleChatterbox(event: ServerChatEvent) {
		val chatterBox = ModGenes.CHATTERBOX.getHolderOrThrow(event.player.registryAccess())
		if (chatterBox.isDisabled) return

		val player = event.player
		if (!player.hasGene(ModGenes.CHATTERBOX)) return

		val message = event.message

		val packet = NarratorPacket(message.string)
		packet.messageAllPlayersTrackingEntityAndSelf(player)
	}

	private val RANDOM_CRINGE_PHRASES = listOf(
		"UwU",
		"owo",
		"OwO",
		"uwu",
		">w<",
		"^w^",
		":3",
		"^-^",
		"^_^",
		"^w^",
		":3"
	)

	/**
	 * [From Create: Estrogen](https://github.com/MayaqqDev/Estrogen/blob/architectury-1.20.1/common/src/main/java/dev/mayaqq/estrogen/utils/UwUfy.java#L18)
	 */
	fun uwufyString(pInput: String): String {
		var input = pInput
		val stringLength: Int = input.length

		input = input
			.replace(Regex("[rl]"), "w")
			.replace(Regex("[RL]"), "W")
			.replace("ove", "uv")
			.replace("o", "owo")
			.replace("O", "OwO")
			.replace("!", "!!!")
			.replace("?", "???")

		if (stringLength % 3 == 0) {
			input = input.uppercase()
		}

		input = if (stringLength % 2 == 0) {
			input.replace(
				Regex("([a-zA-Z])(\\b)"),
				"$1$1$1$1$2"
			)
		} else {
			// 50% chance to duplicate the first letter and add '-'
			input.replace(
				Regex("\\b([a-zA-Z])([a-zA-Z]*)\\b"),
				"$1-$1$2"
			)
		}

		val tildes = "~".repeat(Random.nextInt(0, 4))

		return input + "$tildes " + RANDOM_CRINGE_PHRASES.random()
	}

	fun handleCringeChat(event: ServerChatEvent) {
		val cringe = ModGenes.CRINGE.getHolderOrThrow(event.player.registryAccess())
		if (cringe.isDisabled) return

		val player = event.player
		if (!player.hasGene(ModGenes.CRINGE)) return

		val input = event.message.string
		event.message = Component.literal(uwufyString(input))
	}

	fun handleSlimyChat(event: ServerChatEvent) {
		val player = event.player

		val slimyDeath = ModGenes.SLIMY_DEATH.getHolderOrThrow(player.registryAccess())
		if (slimyDeath.isDisabled) return

		if (!player.hasGene(ModGenes.SLIMY_DEATH)) return

		val nearbySupportSlimes = player.level().getEntities(
			player,
			player.boundingBox.inflate(64.0)
		).filter { it is SupportSlime && it.getOwnerUuid() == player.uuid }

		val amountSlimes = nearbySupportSlimes.size
		val allPlayers = player.server.playerList.players

		for (i in 0 until amountSlimes) {
			val message = Component
				.literal("<")
				.append(
					ModMessageLang.SLIME_SPAM.toComponent(
						player.displayName,
						i + 1
					)
				)
				.append(Component.literal("> "))
				.append(event.message)

			player.level().scheduleTaskInTicks(i + 1) {
				allPlayers.forEach {
					it.sendSystemMessage(message)
				}
			}
		}
	}

	fun handleWallClimbing(player: Player) {
		val wallClimbing = ModGenes.WALL_CLIMBING.getHolderOrThrow(player.registryAccess())
		if (wallClimbing.isDisabled || !player.hasGene(ModGenes.WALL_CLIMBING)) return

		if (player.horizontalCollision || player.minorHorizontalCollision) {
			player.setDeltaMovement(
				player.deltaMovement.x,
				if (player.isCrouching) 0.0 else ServerConfig.CONFIG.wallClimbSpeed.get(),
				player.deltaMovement.z
			)

			player.fallDistance = 0.0f
		}

		if (shouldClingToCeiling(player)) {
			player.setDeltaMovement(
				player.deltaMovement.x,
				ServerConfig.CONFIG.wallClimbSpeed.get(),
				player.deltaMovement.z
			)

			player.pose = Pose.SWIMMING
			player.refreshDimensions()

			val startPos = player.position()

			var moveTries = 0
			while (!shouldClingToCeiling(player) && moveTries < 100) {
				player.setPos(player.x, player.y + 0.01, player.z)
				moveTries++
			}

			if (moveTries >= 100) {
				player.setPos(startPos.x, startPos.y, startPos.z)
			}

			player.fallDistance = 0.0f
		}
	}

	fun shouldClingToCeiling(entity: LivingEntity): Boolean {
		if (!entity.isShiftKeyDown) return false

		val wallClimbing = ModGenes.WALL_CLIMBING.getHolderOrThrow(entity.registryAccess())
		if (wallClimbing.isDisabled || !entity.hasGene(ModGenes.WALL_CLIMBING)) return false

		val entityAabb = entity.boundingBox
		val shrunkenAabb = entityAabb.deflate(entityAabb.xsize * 0.2, 0.0, entityAabb.zsize * 0.2)
		val aboveAabb = AABB(
			shrunkenAabb.minX,
			shrunkenAabb.maxY - 0.1,
			shrunkenAabb.minZ,
			shrunkenAabb.maxX,
			shrunkenAabb.maxY + 0.5,
			shrunkenAabb.maxZ
		)

		val level = entity.level()

		val positions = BlockPos.betweenClosedStream(aboveAabb)

		return positions.anyMatch {
			!level.getBlockState(it).getCollisionShape(level, it).isEmpty
		}
	}

	fun shouldMobGlowFromMobSight(entityToGlow: LivingEntity): Boolean {
		val mobSight = ModGenes.MOB_SIGHT.getHolderOrThrow(entityToGlow.registryAccess())
		if (mobSight.isDisabled) return false

		val localPlayer = AaronClientUtil.localPlayer ?: return false
		if (!localPlayer.hasGene(ModGenes.MOB_SIGHT)) return false

		return entityToGlow.position().closerThan(localPlayer.position(), ServerConfig.CONFIG.mobSightRadius.get())
	}

	@JvmStatic
	fun shouldNegateSlownessFromBlock(livingEntity: LivingEntity, state: BlockState): Boolean {
		if (state.`is`(Blocks.COBWEB)) {
			val webWalker = ModGenes.WEB_WALKER.getHolderOrThrow(livingEntity.registryAccess())
			if (webWalker.isDisabled) return false

			if (livingEntity.hasGene(ModGenes.WEB_WALKER)) {
				return true
			}
		}

		return false
	}

	private var isFertileCloning = false
	fun handleFertile(event: BabyEntitySpawnEvent) {
		if (isFertileCloning) return

		val parentA = event.parentA as? Animal ?: return
		val parentB = event.parentB as? Animal ?: return

		val level = parentA.level() as? ServerLevel ?: return

		val fertile = ModGenes.FERTILE.getHolderOrThrow(parentA.registryAccess())
		if (fertile.isDisabled) return

		var extraBabies = 0
		if (parentA.hasGene(ModGenes.FERTILE)) extraBabies++
		if (parentB.hasGene(ModGenes.FERTILE)) extraBabies++
		if (extraBabies == 0) return

		isFertileCloning = true
		for (i in 0 until extraBabies) {
			parentA.spawnChildFromBreeding(level, parentB)
		}
		isFertileCloning = false
	}

}