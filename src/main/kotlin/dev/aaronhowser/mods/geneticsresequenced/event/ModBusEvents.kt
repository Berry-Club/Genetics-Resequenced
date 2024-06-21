package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapabilityProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.registries.DataPackRegistryEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    bus = EventBusSubscriber.Bus.MOD
)
object ModBusEvents {

    @SubscribeEvent
    fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {

        for (entityType in BuiltInRegistries.ENTITY_TYPE) {
            event.registerEntity(
                GenesCapabilityProvider.geneCapability,
                entityType
            ) { entity, _ -> entity }
        }

    }

    @SubscribeEvent
    fun registerDataPackRegistries(event: DataPackRegistryEvent.NewRegistry) {
//        event.dataPackRegistry(Registries.ENCHANTMENT, Enchantment.DIRECT_CODEC)
//        event.dataPackRegistry(Registries.RECIPE, Recipe.CODEC)
//        event.dataPackRegistry(Registries.ADVANCEMENT, Advancement.CODEC)
    }


}