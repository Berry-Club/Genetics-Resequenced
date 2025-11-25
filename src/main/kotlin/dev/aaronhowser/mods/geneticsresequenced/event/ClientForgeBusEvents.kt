package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.control.ModKeyMappings
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.OtherGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.menu.advanced_incubator.AdvancedIncubatorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_injector.PlasmidInjectorMenu
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.FireballPacket
import dev.aaronhowser.mods.geneticsresequenced.packet.client_to_server.TeleportPlayerPacket
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import net.minecraft.client.model.HumanoidModel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.FORGE
)
class ClientForgeBusEvents {

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
		}
	}

	@SubscribeEvent
	fun onLeaveServer(event: ClientPlayerNetworkEvent.LoggingOut) {
		ClientUtil.addSkinLayersBack()
		ClientUtil.handleCringe(false, 0)
	}

	@SubscribeEvent
	fun beforeRenderEntity(event: RenderLivingEvent.Pre<LivingEntity, HumanoidModel<LivingEntity>>) {
		val entity = event.entity

		if (OtherGenes.shouldClingToCeiling(entity)) {
			val poseStack = event.poseStack
			poseStack.pushPose()
			poseStack.translate(0.0, entity.bbHeight.toDouble(), 0.0)
			poseStack.scale(1.0f, -1.0f, 1.0f)
		}
	}

	@SubscribeEvent
	fun afterRenderLiving(event: RenderLivingEvent.Post<LivingEntity, HumanoidModel<LivingEntity>>) {
		if (OtherGenes.shouldClingToCeiling(event.entity)) {
			event.poseStack.popPose()
		}
	}

}