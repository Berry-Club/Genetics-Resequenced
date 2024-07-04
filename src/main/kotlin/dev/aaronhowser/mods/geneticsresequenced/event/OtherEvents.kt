package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.command.ModCommands
import dev.aaronhowser.mods.geneticsresequenced.data.GeneRequirementRegistry
import dev.aaronhowser.mods.geneticsresequenced.data.MobGeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.AddReloadListenerEvent
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherEvents {

    @SubscribeEvent
    fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
        ModCommands.register(event.dispatcher)
    }

    @SubscribeEvent
    fun addReloadListeners(event: AddReloadListenerEvent) {
        event.addListener(MobGeneRegistry())
        event.addListener(GeneRequirementRegistry())
    }

    @SubscribeEvent
    fun onRegisterBrewingRecipes(event: RegisterBrewingRecipesEvent) {
        BrewingRecipes.setRecipes(event)
    }

}