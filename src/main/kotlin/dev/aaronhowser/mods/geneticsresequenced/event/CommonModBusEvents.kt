package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data.EntityGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.world.entity.EntityType
import net.minecraftforge.event.entity.EntityAttributeModificationEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.registries.DataPackRegistryEvent

@Mod.EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID,
	bus = Mod.EventBusSubscriber.Bus.MOD
)
object CommonModBusEvents {

	@SubscribeEvent
	fun commonSetup(event: FMLCommonSetupEvent) {
		ModPacketHandler.registerPackets(event)
		BrewingRecipes.setRecipes()
	}

	@SubscribeEvent
	fun onNewDataPackRegistry(event: DataPackRegistryEvent.NewRegistry) {
		event.dataPackRegistry(
			ModGenes.GENE_REGISTRY_KEY,
			Gene.DIRECT_CODEC,
			Gene.DIRECT_CODEC
		)

		event.dataPackRegistry(
			EntityGenes.REGISTRY_KEY,
			EntityGenes.CODEC,
			EntityGenes.CODEC
		)
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