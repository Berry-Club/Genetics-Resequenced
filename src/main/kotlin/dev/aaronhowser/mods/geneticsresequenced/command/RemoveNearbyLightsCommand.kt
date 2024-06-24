package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType

object RemoveNearbyLightsCommand {

    private const val RANGE_ARGUMENT = "range"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("removeNearbyLights")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(RANGE_ARGUMENT, IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                    .executes { cmd ->
                        removeNearbyLights(cmd, IntegerArgumentType.getInteger(cmd, RANGE_ARGUMENT))
                    }
            )
            .executes { cmd -> removeNearbyLights(cmd) }
    }

    private fun removeNearbyLights(context: CommandContext<CommandSourceStack>, range: Int = 25): Int {

        val player = context.source.entity as? LivingEntity ?: return 0

        val level = player.level()
        val playerPos = player.blockPosition()

        val lightSpots = mutableSetOf<BlockPos>()

        for (x in -range..range) for (y in -range..range) for (z in -range..range) {
            val pos = BlockPos(playerPos.x + x, playerPos.y + y, playerPos.z + z)

            if (level.getBlockState(pos).block == ModBlocks.BIOLUMINESCENCE_BLOCK.get()) {
                lightSpots.add(pos)
            }
        }

        lightSpots.forEach {
            level.removeBlock(it, false)

            val entity = EntityType.BLOCK_DISPLAY.spawn(
                level as ServerLevel,
                { display ->
                    display.setPos(it.x.toDouble(), it.y.toDouble(), it.z.toDouble())
                    display.setGlowingTag(true)
                },
                it,
                MobSpawnType.COMMAND,
                false,
                false
            )

            if (entity != null) {
                level.addFreshEntity(entity)

                ModScheduler.scheduleTaskInTicks(20 * 10) {
                    entity.remove(Entity.RemovalReason.DISCARDED)
                }
            }

        }

        player.sendSystemMessage(
            Component.translatable(
                ModLanguageProvider.Commands.REMOVED_LIGHTS,
                lightSpots.size
            )
        )

        return 1
    }

}