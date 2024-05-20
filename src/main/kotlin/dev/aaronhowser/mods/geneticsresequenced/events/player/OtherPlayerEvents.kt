package dev.aaronhowser.mods.geneticsresequenced.events.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.ClickGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.GeneChangedPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.player.ArrowLooseEvent
import net.minecraftforge.event.entity.player.ArrowNockEvent
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

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

        if (entity is Player) {
            when (changedGene) {
                DefaultGenes.EFFICIENCY -> {
                    val entityGenes = entity.getGenes() ?: return
                    if (entityGenes.hasGene(DefaultGenes.EFFICIENCY_4)) return
                    AttributeGenes.setEfficiency(entity, if (wasAdded) 1 else 0)
                }

                DefaultGenes.EFFICIENCY_4 -> {
                    if (wasAdded) {
                        AttributeGenes.setEfficiency(entity, 4)
                    } else {
                        val entityGenes = entity.getGenes() ?: return
                        val levelToSetTo = if (entityGenes.hasGene(DefaultGenes.EFFICIENCY)) 1 else 0
                        AttributeGenes.setEfficiency(entity, levelToSetTo)
                    }
                }

                DefaultGenes.STEP_ASSIST -> AttributeGenes.setStepAssist(entity, wasAdded)

                DefaultGenes.WALL_CLIMBING -> AttributeGenes.setWallClimbing(entity, wasAdded)

                DefaultGenes.FLIGHT -> TickGenes.handleFlight(entity)

                else -> {}
            }
        }

        if (!wasAdded && changedGene.getPotion() != null) {
            TickGenes.handlePotionGeneRemoved(entity, changedGene)
        }

    }

    @SubscribeEvent
    fun onPickUpItem(event: ItemPickupEvent) {
        val item = event.stack

        if (item.`is`(ModItems.SYRINGE.get())) {
            val thrower = event.originalEntity.throwingEntity as? Player

            event.entity.hurt(SyringeItem.damageSourceDrop(thrower), 1.0f)
        }

    }

}