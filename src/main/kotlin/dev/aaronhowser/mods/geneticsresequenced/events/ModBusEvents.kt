package dev.aaronhowser.mods.geneticsresequenced.events

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability
import dev.aaronhowser.mods.geneticsresequenced.registries.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.registries.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.entities.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions
import net.minecraft.world.entity.EntityType
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.event.entity.EntityAttributeModificationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.config.ModConfigEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    bus = Mod.EventBusSubscriber.Bus.MOD
)
object ModBusEvents {

    @SubscribeEvent
    fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {
        event.register(GenesCapability::class.java)
    }

    @SubscribeEvent
    fun onEntityAttributeCreation(event: EntityAttributeCreationEvent) {
        event.put(ModEntityTypes.SUPPORT_SLIME.get(), SupportSlime.setAttributes())
    }

    @SubscribeEvent
    fun onEntityAttributeModification(event: EntityAttributeModificationEvent) {
        if (!event.has(EntityType.PLAYER, ModAttributes.EFFICIENCY)) {
            event.add(EntityType.PLAYER, ModAttributes.EFFICIENCY)
        }

        if (!event.has(EntityType.PLAYER, ModAttributes.WALL_CLIMBING)) {
            event.add(EntityType.PLAYER, ModAttributes.WALL_CLIMBING)
        }
    }

    //TODO: Move more things to here
    @SubscribeEvent
    fun onConfig(event: ModConfigEvent) {
        val config = event.config

        // Comparing spec didn't work for some reason
        if (config.modId == GeneticsResequenced.ID && config.type == ModConfig.Type.SERVER) {
            Gene.checkDeactivationConfig()
        }
    }

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        ModPotions.addRecipes()
    }
}