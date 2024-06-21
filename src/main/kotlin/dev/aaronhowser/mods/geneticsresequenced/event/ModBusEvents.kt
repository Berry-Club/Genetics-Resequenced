package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DataPackRegistryEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    bus = EventBusSubscriber.Bus.MOD
)
object ModBusEvents {

    @SubscribeEvent
    fun registerDataPackRegistries(event: DataPackRegistryEvent.NewRegistry) {
//        event.dataPackRegistry(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC)
//        event.dataPackRegistry(Registries.RECIPE, Recipe.CODEC)
//        event.dataPackRegistry(Registries.ADVANCEMENT, Advancement.CODEC)
    }


}