package dev.aaronhowser.mods.geneticsresequenced.events

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability
import dev.aaronhowser.mods.geneticsresequenced.attributes.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.entities.FriendlySlime
import dev.aaronhowser.mods.geneticsresequenced.entities.ModEntityTypes
import net.minecraft.world.entity.EntityType
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.event.entity.EntityAttributeModificationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

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
        event.put(ModEntityTypes.FRIENDLY_SLIME.get(), FriendlySlime.setAttributes())
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
}