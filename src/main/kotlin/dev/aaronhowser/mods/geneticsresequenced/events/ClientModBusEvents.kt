package dev.aaronhowser.mods.geneticsresequenced.events

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.controls.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.registries.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.entities.client.FriendlySlimeRenderer
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registries.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.client.renderer.item.ItemPropertyFunction
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ModelEvent
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = [Dist.CLIENT]
)
object ClientModBusEvents {

    @SubscribeEvent
    fun onKeyRegister(event: RegisterKeyMappingsEvent) {
        event.register(ModKeyMappings.DRAGONS_BREATH)
        event.register(ModKeyMappings.TELEPORT)
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        ModMenuTypes.registerScreens()
        registerEntityRenderers()
    }


    private fun registerEntityRenderers() {
        EntityRenderers.register(ModEntityTypes.SUPPORT_SLIME.get(), ::FriendlySlimeRenderer)
    }

    @SubscribeEvent
    fun onModelRegistry(event: ModelEvent.RegisterAdditional) {

        ItemProperties.register(
            ModItems.SYRINGE.get(),
            OtherUtil.modResource("full"),
            ItemPropertyFunction { stack, _, _, _ ->
                if (SyringeItem.hasBlood(stack)) 1f else 0f
            }
        )

        ItemProperties.register(
            ModItems.SYRINGE.get(),
            OtherUtil.modResource("injecting"),
            ItemPropertyFunction { stack, _, entity, _ ->
                if (SyringeItem.isBeingUsed(stack, entity)) 1f else 0f
            }
        )

        ItemProperties.register(
            ModItems.METAL_SYRINGE.get(),
            OtherUtil.modResource("full"),
            ItemPropertyFunction { stack, _, _, _ ->
                if (SyringeItem.hasBlood(stack)) 1f else 0f
            }
        )

    }

}