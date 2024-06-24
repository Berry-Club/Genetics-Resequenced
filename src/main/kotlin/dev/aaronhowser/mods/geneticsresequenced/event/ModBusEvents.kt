package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.config.ModConfigEvent
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
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

    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        val config = event.config

        // Comparing spec didn't work for some reason
        if (config.modId == GeneticsResequenced.ID && config.type == ModConfig.Type.SERVER) {
            Gene.checkDeactivationConfig()
        }
    }

    @SubscribeEvent
    fun onEntityAttributeCreation(event: EntityAttributeCreationEvent) {
        event.put(ModEntityTypes.SUPPORT_SLIME.get(), SupportSlime.setAttributes())
    }

    @SubscribeEvent
    fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {

        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.COAL_GENERATOR.get()
        ) { coalGen, direction -> coalGen.getItemCapability(direction) }

    }

    @SubscribeEvent
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        ModPacketHandler.registerPayloads(event)
    }

}