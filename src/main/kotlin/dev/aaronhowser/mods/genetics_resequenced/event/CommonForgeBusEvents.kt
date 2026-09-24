package dev.aaronhowser.mods.genetics_resequenced.event

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.genetics_resequenced.command.ModCommands
import dev.aaronhowser.mods.genetics_resequenced.entity.SupportSlime
import dev.aaronhowser.mods.genetics_resequenced.registry.ModEntityTypes
import net.minecraft.world.level.block.entity.BlockEntity
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