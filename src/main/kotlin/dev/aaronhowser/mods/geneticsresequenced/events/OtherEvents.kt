package dev.aaronhowser.mods.geneticsresequenced.events

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneRequirementRegistry
import dev.aaronhowser.mods.geneticsresequenced.commands.ModCommands
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.ServerTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherEvents {

    @SubscribeEvent
    fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
        ModCommands.register(event.dispatcher)
    }

    @SubscribeEvent
    fun onServerTick(event: ServerTickEvent) {
        if (event.phase == TickEvent.Phase.END) ModScheduler.currentTick++
    }

    @SubscribeEvent
    fun addReloadListeners(event: AddReloadListenerEvent) {
        event.addListener(MobGeneRegistry)
        event.addListener(GeneRequirementRegistry)
    }

}