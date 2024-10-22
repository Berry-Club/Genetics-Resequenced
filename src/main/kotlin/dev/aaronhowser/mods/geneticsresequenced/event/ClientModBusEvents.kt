package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.control.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.entity.client.SupportSlimeRenderer
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModCreativeModeTabs
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.client.renderer.item.ItemPropertyFunction
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.ModelEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID,
    bus = EventBusSubscriber.Bus.MOD,
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
        registerEntityRenderers()
    }

    private fun registerEntityRenderers() {
        EntityRenderers.register(ModEntityTypes.SUPPORT_SLIME.get(), ::SupportSlimeRenderer)
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

    @SubscribeEvent
    fun onRegisterMenuScreens(event: RegisterMenuScreensEvent) {
        ModMenuTypes.registerScreens(event)
    }

    @SubscribeEvent
    fun addToCreativeTab(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.FRIENDLY_SLIME_SPAWN_EGG.get())
        }

        val registryAccess = ClientUtil.localRegistryAccess
        if (event.tab == ModCreativeModeTabs.MOD_TAB.get() && registryAccess != null) {
            event.acceptAll(DnaHelixItem.getAllHelices(registryAccess))
            event.acceptAll(PlasmidItem.getAllPlasmids(registryAccess))
        }
    }

}