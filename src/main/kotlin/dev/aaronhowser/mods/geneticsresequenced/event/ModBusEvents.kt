package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes
import dev.aaronhowser.mods.geneticsresequenced.attribute.ModAttributes
import net.minecraft.world.entity.EntityType
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent
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
        event.register(Genes::class.java)
    }

    @SubscribeEvent
    fun onEntityAttribute(event: EntityAttributeModificationEvent) {

        if (!event.has(EntityType.PLAYER, ModAttributes.EFFICIENCY_ATTRIBUTE)) {
            event.add(EntityType.PLAYER, ModAttributes.EFFICIENCY_ATTRIBUTE)
        }

        if (!event.has(EntityType.PLAYER, ModAttributes.CLIMBING_ATTRIBUTE)) {
            event.add(EntityType.PLAYER, ModAttributes.CLIMBING_ATTRIBUTE)
        }

    }
}