package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block_old.machine.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.block_old.machine.incubator_advanced.AdvancedIncubatorMenu
import dev.aaronhowser.mods.geneticsresequenced.block_old.machine.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.block_old.machine.plasmid_injector.PlasmidInjectorMenu
import dev.aaronhowser.mods.geneticsresequenced.control.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.entity.client.SupportSlimeRenderer
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.world.inventory.AbstractContainerMenu
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

@EventBusSubscriber(
	modid = GeneticsResequenced.ID,
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
			else -> return
		}
	}

	@SubscribeEvent
	fun onLeaveServer(event: ClientPlayerNetworkEvent.LoggingOut) {
		ClientUtil.addSkinLayersBack()
		ClientUtil.handleCringe(false, 0)
	}

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
			OtherUtil.modResource("full")
		) { stack, _, _, _ ->
			if (SyringeItem.hasBlood(stack)) 1f else 0f
		}

		ItemProperties.register(
			ModItems.SYRINGE.get(),
			OtherUtil.modResource("injecting")
		) { stack, _, entity, _ ->
			if (SyringeItem.isBeingUsed(stack, entity)) 1f else 0f
		}

		ItemProperties.register(
			ModItems.METAL_SYRINGE.get(),
			OtherUtil.modResource("full")
		) { stack, _, _, _ ->
			if (SyringeItem.hasBlood(stack)) 1f else 0f
		}

	}

	@SubscribeEvent
	fun onRegisterMenuScreens(event: RegisterMenuScreensEvent) {
		ModMenuTypes.registerScreens(event)
	}

}