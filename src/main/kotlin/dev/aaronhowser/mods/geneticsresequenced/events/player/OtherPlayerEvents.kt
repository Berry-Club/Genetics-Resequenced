package dev.aaronhowser.mods.geneticsresequenced.events.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.ClickGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.player.ArrowLooseEvent
import net.minecraftforge.event.entity.player.ArrowNockEvent
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import kotlin.random.Random

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherPlayerEvents {

    @SubscribeEvent
    fun onInteractWithBlock(event: PlayerInteractEvent.RightClickBlock) {
        ClickGenes.eatGrass(event)
    }

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent) {
        TickGenes.handleNoHunger(event.player)
        TickGenes.handleMobSight(event.player)
        AttributeGenes.handleWallClimbing(event.player)     // Requires clientside handling
        TickGenes.handleItemMagnet(event.player)
        TickGenes.handleXpMagnet(event.player)
    }

    @SubscribeEvent
    fun onArrowNock(event: ArrowNockEvent) {
        ClickGenes.handleInfinityStart(event)
    }

    @SubscribeEvent
    fun onArrowLoose(event: ArrowLooseEvent) {
        ClickGenes.handleInfinityEnd(event)
    }

    @SubscribeEvent
    fun onLogIn(event: PlayerLoggedInEvent) {
        val player = event.entity as? ServerPlayer ?: return
        val genes = player.getGenes() ?: return

        for (gene in genes.getGeneList()) {
            ModPacketHandler.messagePlayer(
                player,
                GeneChangedPacket(gene.id, true)
            )
        }
    }

    fun genesChanged(entity: LivingEntity, changedGene: Gene, wasAdded: Boolean) {

        //TODO: Also sync OTHER ENTITY'S genes to the client
        if (entity is ServerPlayer) {
            ModPacketHandler.messagePlayer(
                entity,
                GeneChangedPacket(changedGene.id, wasAdded)
            )
        }

        val entityGenes = entity.getGenes() ?: return

        if (entity is Player) {
            when (changedGene) {
                DefaultGenes.efficiency -> {
                    if (entityGenes.hasGene(DefaultGenes.efficiencyFour)) return
                    val levelToSetTo = if (wasAdded) 1 else 0
                    AttributeGenes.setEfficiency(entity, levelToSetTo)
                }

                DefaultGenes.efficiencyFour -> {
                    if (wasAdded) {
                        AttributeGenes.setEfficiency(entity, 4)
                    } else {
                        val levelToSetTo = if (entityGenes.hasGene(DefaultGenes.efficiency)) 1 else 0
                        AttributeGenes.setEfficiency(entity, levelToSetTo)
                    }
                }

                DefaultGenes.stepAssist -> AttributeGenes.setStepAssist(entity, wasAdded)

                DefaultGenes.wallClimbing -> AttributeGenes.setWallClimbing(entity, wasAdded)

                DefaultGenes.moreHearts -> {
                    AttributeGenes.setMoreHearts(entity, 1, wasAdded)
                }

                DefaultGenes.moreHeartsTwo -> {
                    AttributeGenes.setMoreHearts(entity, 2, wasAdded)
                }

                DefaultGenes.flight -> TickGenes.handleFlight(entity)

                else -> {}
            }
        }

        if (!wasAdded && changedGene.getPotion() != null) {
            TickGenes.handlePotionGeneRemoved(entity, changedGene)
        }

        ModScheduler.scheduleTaskInTicks(1) {
            checkForMissingRequirements(entity)
        }
    }

    private fun checkForMissingRequirements(entity: LivingEntity) {
        val entityGenes = entity.getGenes() ?: return

        val newGenes = entityGenes.getGeneList()

        val genesWithMissingRequirements = newGenes.filter { gene ->
            !gene.getRequiredGenes().all { it in newGenes }
        }

        genesWithMissingRequirements.forEach { gene ->
            entityGenes.removeGene(gene)
            genesChanged(entity, gene, false)

            val requiredGenesComponent =
                Component.translatable("message.geneticsresequenced.gene_missing_requirements.list")

            for (requiredGene in gene.getRequiredGenes()) {
                val hasGene = newGenes.contains(requiredGene)
                if (hasGene) continue

                requiredGenesComponent.append(Component.literal("\n - ").append(requiredGene.nameComponent))
            }

            entity.sendSystemMessage(
                Component.translatable(
                    "message.geneticsresequenced.gene_missing_requirements",
                    gene.nameComponent
                ).withStyle { it.withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, requiredGenesComponent)) }
            )
        }
    }


    @SubscribeEvent
    fun onPickUpItem(event: ItemPickupEvent) {
        val item = event.stack

        if (item.`is`(ModItems.SYRINGE.get())) {
            val thrower = event.originalEntity.throwingEntity as? Player

            event.entity.hurt(SyringeItem.damageSourceStepOnSyringe(thrower), 1.0f)
        }

    }

    private val villagerSounds = listOf(
        SoundEvents.VILLAGER_TRADE,
        SoundEvents.VILLAGER_AMBIENT,
        SoundEvents.VILLAGER_CELEBRATE
    )

    @SubscribeEvent
    fun onChatMessage(event: ServerChatEvent.Submitted) {
        if (Random.nextDouble() > ServerConfig.emeraldHeartChatChance.get()) return

        val player = event.player
        val genes = player.getGenes() ?: return

        if (genes.hasGene(DefaultGenes.emeraldHeart)) {
            player.level.playSound(
                null,
                player.blockPosition(),
                villagerSounds.random(),
                player.soundSource,
                1f,
                1f
            )
        }

    }

}