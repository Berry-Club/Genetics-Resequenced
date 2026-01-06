package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.client.renderer.GeneRenderChanges
import dev.aaronhowser.mods.geneticsresequenced.client.renderer.entity.SupportSlimeRenderer
import dev.aaronhowser.mods.geneticsresequenced.control.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.menu.advanced_incubator.AdvancedIncubatorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_injector.PlasmidInjectorMenu
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.client.event.ModelEvent
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID,
	value = [Dist.CLIENT]
)
object ClientForgeBusEvents {

	@SubscribeEvent
	fun onKeyInputEvent(event: InputEvent.Key) {
		if (ModKeyMappings.TELEPORT.consumeClick()) {
			ModPacketHandler.messageServer(TeleportPlayerPacket.INSTANCE)
		}

		if (ModKeyMappings.DRAGONS_BREATH.consumeClick()) {
			ModPacketHandler.messageServer(FireballPacket.INSTANCE)
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

	@SubscribeEvent
	fun beforeRenderEntity(event: RenderLivingEvent.Pre<LivingEntity, HumanoidModel<LivingEntity>>) {
		GeneRenderChanges.spiderClimbFlip(event)
		GeneRenderChanges.shakeFromCringe(event)
	}

	@SubscribeEvent
	fun afterRenderLiving(event: RenderLivingEvent.Post<LivingEntity, HumanoidModel<LivingEntity>>) {
		GeneRenderChanges.spiderClimbFlipPost(event)
		GeneRenderChanges.shakeFromCringePost(event)
	}

}