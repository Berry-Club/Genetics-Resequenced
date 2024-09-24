package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_injector.PlasmidInjectorMenu
import dev.aaronhowser.mods.geneticsresequenced.control.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import net.minecraft.world.inventory.AbstractContainerMenu
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    value = [Dist.CLIENT]
)
object ClientEvents {

    @SubscribeEvent
    fun onKeyInputEvent(event: InputEvent.Key) {
        if (ModKeyMappings.TELEPORT.consumeClick()) {
            ModPacketHandler.messageServer(TeleportPlayerPacket.INSTANCE)
        }

        if (ModKeyMappings.DRAGONS_BREATH.consumeClick()) {
            ModPacketHandler.messageServer(FireballPacket.INSTANCE)
        }
    }

    @SubscribeEvent
    fun tooltip(event: ItemTooltipEvent) {
        BrewingRecipes.tooltip(event)
        TickGenes.itemMagnetBlacklistTooltip(event)

        handleScreens(event)
    }

    private fun handleScreens(event: ItemTooltipEvent) {
        val screen: AbstractContainerMenu = event.entity?.containerMenu ?: return

        when (screen) {
            is CoalGeneratorMenu -> CoalGeneratorMenu.showFuelTooltip(event)
            is PlasmidInfuserMenu -> PlasmidInfuserMenu.showTooltip(event)
            is PlasmidInjectorMenu -> PlasmidInjectorMenu.showTooltip(event)
            is AdvancedIncubatorMenu -> AdvancedIncubatorMenu.showChanceTooltip(event)
            else -> return
        }
    }

    @SubscribeEvent
    fun onLeaveServer(event: ClientPlayerNetworkEvent.LoggingOut) {
        ClientUtil.addSkinLayersBack()
        ClientUtil.handleCringe(false, 0)
    }

}