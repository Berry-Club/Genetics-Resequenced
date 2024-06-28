package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Display.BlockDisplay
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.block.Blocks

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

        if (range !in 1..100) {
            player.sendSystemMessage(
                ModLanguageProvider.Commands.REMOVED_LIGHTS_RANGE_TOO_HIGH
                    .toComponent(100)
            )
            return 0
        }


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

            val blockDisplayEntity = BlockDisplay(EntityType.BLOCK_DISPLAY, level).apply {
                setPos(it.x.toDouble(), it.y.toDouble(), it.z.toDouble())
                setGlowingTag(true)
                setBlockState(Blocks.GLOWSTONE.defaultBlockState())
            }
            level.addFreshEntity(blockDisplayEntity)

            ModScheduler.scheduleTaskInTicks(20 * 5) {
                blockDisplayEntity.remove(Entity.RemovalReason.DISCARDED)
            }
        }

        player.sendSystemMessage(
            ModLanguageProvider.Commands.REMOVED_LIGHTS.toComponent(
                lightSpots.size
            )
        )

        return 1
    }

}