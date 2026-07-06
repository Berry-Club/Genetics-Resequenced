package dev.aaronhowser.mods.genetics_resequenced.event

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.client.renderer.GeneRenderChanges
import dev.aaronhowser.mods.genetics_resequenced.client.renderer.entity.SupportSlimeRenderer
import dev.aaronhowser.mods.genetics_resequenced.control.ModKeyMappings
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.genetics_resequenced.menu.advanced_incubator.AdvancedIncubatorMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.genetics_resequenced.menu.plasmid_injector.PlasmidInjectorMenu
import dev.aaronhowser.mods.genetics_resequenced.packet.client_to_server.FireballPacket
import dev.aaronhowser.mods.genetics_resequenced.packet.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.genetics_resequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModMenuTypes
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.world.inventory.AbstractContainerMenu
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

@EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID,
	value = [Dist.CLIENT]
)
object ClientEvents {

	@SubscribeEvent
	fun onKeyInputEvent(event: InputEvent.Key) {
		if (ModKeyMappings.TELEPORT.consumeClick()) {
			TeleportPlayerPacket.INSTANCE.messageServer()
		}

		if (ModKeyMappings.DRAGONS_BREATH.consumeClick()) {
			FireballPacket.INSTANCE.messageServer()
		}
	}

	@SubscribeEvent
	fun registerKeyMappings(event: RegisterKeyMappingsEvent) {
		event.registerCategory(ModKeyMappings.CATEGORY)
		event.register(ModKeyMappings.TELEPORT)
		event.register(ModKeyMappings.DRAGONS_BREATH)
	}

	@SubscribeEvent
	fun tooltip(event: ItemTooltipEvent) {
		BrewingRecipes.tooltip(event)
		TickGenes.itemMagnetBlacklistTooltip(event)

		handleScreens(event)
	}

	private fun handleScreens(event: ItemTooltipEvent) {
		val screen: AbstractContainerMenu = event.entity?.containerMenu ?: return

		when (screen) {
			is CoalGeneratorMenu -> CoalGeneratorMenu.showFuelTooltip(event)
			is PlasmidInfuserMenu -> PlasmidInfuserMenu.showTooltip(event)
			is PlasmidInjectorMenu -> PlasmidInjectorMenu.showTooltip(event)
			is AdvancedIncubatorMenu -> AdvancedIncubatorMenu.showChanceTooltip(event)
		}
	}

	@SubscribeEvent
	fun onLeaveServer(event: ClientPlayerNetworkEvent.LoggingOut) {
		ClientUtil.addSkinLayersBack()
		ClientUtil.handleCringe(false, 0)
	}

	@SubscribeEvent
	fun onClientSetup(event: FMLClientSetupEvent) {
		registerEntityRenderers()
	}

	@SubscribeEvent
	fun onRegisterRenderStateModifiers(event: RegisterRenderStateModifiersEvent) {
		GeneRenderChanges.registerRenderStateModifiers(event)
	}

	private fun registerEntityRenderers() {
		EntityRenderers.register(ModEntityTypes.SUPPORT_SLIME.get(), ::SupportSlimeRenderer)
	}

	@SubscribeEvent
	fun onRegisterMenuScreens(event: RegisterMenuScreensEvent) {
		ModMenuTypes.registerScreens(event)
	}

	@SubscribeEvent
	fun beforeRenderEntity(event: RenderLivingEvent.Pre<*, *, *>) {
		GeneRenderChanges.spiderClimbFlip(event)
		GeneRenderChanges.shakeFromCringe(event)
	}

	@SubscribeEvent
	fun afterRenderLiving(event: RenderLivingEvent.Post<*, *, *>) {
		GeneRenderChanges.spiderClimbFlipPost(event)
		GeneRenderChanges.shakeFromCringePost(event)
	}

}
