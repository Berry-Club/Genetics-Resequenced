package dev.aaronhowser.mods.geneticsresequenced.events

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.controls.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packets.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.util.ClientHelper
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    value = [Dist.CLIENT]
)
object ClientEvents {

    @SubscribeEvent
    fun onKeyInputEvent(event: InputEvent.Key) {
        if (ModKeyMappings.TELEPORT.consumeClick()) {
            ModPacketHandler.messageServer(TeleportPlayerPacket())
        }

        if (ModKeyMappings.DRAGONS_BREATH.consumeClick()) {
            ModPacketHandler.messageServer(FireballPacket())
        }
    }

    @SubscribeEvent
    fun onItemTooltipEvent(event: ItemTooltipEvent) {
        val screen: AbstractContainerMenu = event.entity?.containerMenu ?: return

        when (screen) {
            is CoalGeneratorMenu -> CoalGeneratorMenu.showFuelTooltip(event)
            is PlasmidInfuserMenu -> PlasmidInfuserMenu.showTooltip(event)
            else -> return
        }
    }

    @SubscribeEvent
    fun onLeaveServer(event: PlayerLoggedOutEvent) {
        ClientHelper.addSkinLayersBack()
    }

}