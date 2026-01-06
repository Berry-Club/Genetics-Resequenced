package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.event.entity.EntityAttributeModificationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.registries.DataPackRegistryEvent

@Mod.EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.MOD
)
object CommonModBusEvents {

	@SubscribeEvent
	fun onNewDataPackRegistry(event: DataPackRegistryEvent.NewRegistry) {
		event.dataPackRegistry(
			ModGenes.GENE_REGISTRY_KEY,
			Gene.DIRECT_CODEC,
			Gene.DIRECT_CODEC
		)
	}

	@SubscribeEvent
	fun onEntityAttributeCreation(event: EntityAttributeCreationEvent) {
		event.put(ModEntityTypes.SUPPORT_SLIME.get(), SupportSlime.setAttributes())
	}

	@SubscribeEvent
	fun onAttachBlockEntityCapabilities(event: AttachCapabilitiesEvent<in BlockEntity>) {
		val modBlockEntityTypes = ModBlockEntityTypes.BLOCK_ENTITY_REGISTRY.entries

		for (deferredBeType in modBlockEntityTypes) {
			val blockEntityType = deferredBeType.get()

			event.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK,
				blockEntityType
			) { blockEntity, direction ->
				if (blockEntity is MachineBlockEntity) {
					blockEntity.getItemHandler(direction)
				} else null
			}

			event.registerBlockEntity(
				Capabilities.EnergyStorage.BLOCK,
				blockEntityType
			) { blockEntity, direction ->
				if (blockEntity is MachineBlockEntity) {
					blockEntity.getEnergyCapability(direction)
				} else null
			}
		}
	}

	@SubscribeEvent
	fun onEntityAttributeModification(event: EntityAttributeModificationEvent) {
		if (!event.has(EntityType.PLAYER, ModAttributes.EFFICIENCY.get())) {
			event.add(EntityType.PLAYER, ModAttributes.EFFICIENCY.get())
		}

		for (type in event.types) {
			if (!event.has(type, ModAttributes.BASE_LOOTING.get())) {
				event.add(type, ModAttributes.BASE_LOOTING.get())
			}

			if (!event.has(type, ModAttributes.XP_DROP_MULTIPLIER.get())) {
				event.add(type, ModAttributes.XP_DROP_MULTIPLIER.get())
			}
		}
	}

}