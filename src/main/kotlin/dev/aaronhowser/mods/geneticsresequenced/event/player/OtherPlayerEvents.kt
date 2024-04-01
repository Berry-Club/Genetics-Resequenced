package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherPlayerEvents {

    @SubscribeEvent
    fun onPlayerCloned(event: PlayerEvent.Clone) {
        if (!event.isWasDeath) return

        event.original.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { oldGenes ->
            event.original.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { newGenes ->
                newGenes.setGeneList(oldGenes.getGeneList())
            }
        }
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        handleKeepGenesOnDeath(event)
    }

    private fun handleKeepGenesOnDeath(event: PlayerRespawnEvent) {
        if (ServerConfig.keepGenesOnDeath.get()) return

        val player = event.entity
        val playerGenes = player.getGenes() ?: return

        if (playerGenes.getGeneList().isEmpty()) return

        val component = Component
            .literal("Genetics Resequenced")
            .withStyle {
                it
                    .withColor(ChatFormatting.DARK_PURPLE)
                    .withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, Component
                                .literal(playerGenes.getGeneList().joinToString(", "))
                        )
                    )
            }
            .append(
                Component
                    .literal(": Your genes have been removed on death.")
                    .withStyle {
                        it
                            .withColor(ChatFormatting.GRAY)
                    }
            )
        player.sendSystemMessage(component)

        playerGenes.removeAllGenes()
    }

    @SubscribeEvent
    fun onInteractWithBlock(event: PlayerInteractEvent.RightClickBlock) {
        if (event.side.isClient) return
        val player = event.entity

        val genes = player.getGenes() ?: return

        if (genes.hasGene(EnumGenes.EAT_GRASS)) eatGrass(event)
    }

    private fun eatGrass(event: PlayerInteractEvent.RightClickBlock) {

        if (!event.itemStack.isEmpty) return

        val player = event.entity

        val isHungry = player.foodData.foodLevel < 20
        if (!isHungry) return

        val block = event.level.getBlockState(event.pos).block

        val blockAfter = when (block) {
            Blocks.GRASS_BLOCK, Blocks.MYCELIUM -> Blocks.DIRT
            Blocks.WARPED_NYLIUM, Blocks.CRIMSON_NYLIUM -> Blocks.NETHERRACK
            else -> return
        }

        event.level.setBlockAndUpdate(event.pos, blockAfter.defaultBlockState())
        player.foodData.eat(1, 0.1f)

        event.level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.PLAYER_BURP,
            SoundSource.PLAYERS,
            1.0f,
            1.0f
        )

        event.level.playSound(
            null,
            event.pos,
            SoundEvents.GRASS_BREAK,
            SoundSource.BLOCKS,
            1.0f,
            1.0f
        )

    }

}