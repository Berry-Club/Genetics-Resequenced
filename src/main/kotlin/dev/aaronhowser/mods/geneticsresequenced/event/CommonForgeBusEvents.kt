package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.command.ModCommands
import dev.aaronhowser.mods.geneticsresequenced.data.EntityGenes
import dev.aaronhowser.mods.geneticsresequenced.data.GeneRequirements
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.event.AddReloadListenerEvent
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.FORGE
)
object CommonForgeBusEvents {

	@SubscribeEvent
	fun onRegisterCommandsEvent(event: RegisterCommandsEvent) {
		ModCommands.register(event.dispatcher)
	}

	@SubscribeEvent
	fun addReloadListeners(event: AddReloadListenerEvent) {
		event.addListener(EntityGenes())
		event.addListener(GeneRequirements())
	}

	@SubscribeEvent
	fun onEntityAttributeCreation(event: EntityAttributeCreationEvent) {
		event.put(ModEntityTypes.SUPPORT_SLIME.get(), SupportSlime.setAttributes())
	}

	@SubscribeEvent
	fun onAttachBlockEntityCapabilities(event: AttachCapabilitiesEvent<BlockEntity>) {
		val blockEntity = event.getObject()

		if (blockEntity !is MachineBlockEntity) {
			return
		}


	}

}