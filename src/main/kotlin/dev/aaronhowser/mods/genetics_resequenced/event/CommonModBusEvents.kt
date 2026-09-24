package dev.aaronhowser.mods.genetics_resequenced.event

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.data.EntityGenes
import dev.aaronhowser.mods.genetics_resequenced.data.GeneRequirements
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.genetics_resequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModAttributes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
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

		event.dataPackRegistry(
			GeneRequirements.REGISTRY_KEY,
			GeneRequirements.CODEC,
			GeneRequirements.CODEC
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