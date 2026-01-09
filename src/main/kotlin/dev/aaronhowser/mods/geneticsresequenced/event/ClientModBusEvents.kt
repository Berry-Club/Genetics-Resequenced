package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.client.renderer.entity.SupportSlimeRenderer
import dev.aaronhowser.mods.geneticsresequenced.control.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraftforge.client.event.ModelEvent
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.MOD
)
object ClientModBusEvents {

	@SubscribeEvent
	fun onClientSetup(event: FMLClientSetupEvent) {
		registerEntityRenderers()
		ModMenuTypes.registerScreens(event)
	}

	private fun registerEntityRenderers() {
		EntityRenderers.register(ModEntityTypes.SUPPORT_SLIME.get(), ::SupportSlimeRenderer)
	}

	@SubscribeEvent
	fun onKeyRegister(event: RegisterKeyMappingsEvent) {
		event.register(ModKeyMappings.DRAGONS_BREATH)
		event.register(ModKeyMappings.TELEPORT)
	}

	@SubscribeEvent
	fun onModelRegistry(event: ModelEvent.RegisterAdditional) {

		ItemProperties.register(
			ModItems.SYRINGE.get(),
			GeneticsResequenced.modResource("full")
		) { stack, _, _, _ ->
			if (SyringeItem.hasBlood(stack)) 1f else 0f
		}

		ItemProperties.register(
			ModItems.SYRINGE.get(),
			GeneticsResequenced.modResource("injecting")
		) { stack, _, entity, _ ->
			if (SyringeItem.isBeingUsed(stack, entity)) 1f else 0f
		}

		ItemProperties.register(
			ModItems.METAL_SYRINGE.get(),
			GeneticsResequenced.modResource("full")
		) { stack, _, _, _ ->
			if (SyringeItem.hasBlood(stack)) 1f else 0f
		}
	}


}