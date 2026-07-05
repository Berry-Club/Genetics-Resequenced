package dev.aaronhowser.mods.genetics_resequenced.datagen

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.datapack.ModDamageTypeProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.datapack.ModEnchantmentProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.gene.ModEntityGenesProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.gene.ModGeneProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.gene.ModGeneRequirementsProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.loot.ModLootTableProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.model.ModModelProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.recipe.ModRecipeProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.tag.*
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.advancements.AdvancementProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = GeneticsResequenced.MOD_ID)
object ModDataGen {

	@SubscribeEvent
	fun onGatherClientData(event: GatherDataEvent.Client) {
		val output = event.generator.packOutput
		val baseLookupProvider = event.lookupProvider

		// Assets

		event.addProvider(ModModelProvider(output))
		event.addProvider(ModLanguageProvider(output))

		// Data

		event.createDatapackRegistryObjects(
			RegistrySetBuilder()
				.add(ModGenes.GENE_REGISTRY_KEY, ModGeneProvider::bootstrap)
				.add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::bootstrap)
				.add(Registries.ENCHANTMENT, ModEnchantmentProvider::bootstrap)
		)

		val lookupWithDatapack = event.lookupProvider

		event.addProvider(ModRecipeProvider.Runner(output, lookupWithDatapack))

		event.addProvider(ModBlockTagsProvider(output, baseLookupProvider))
		event.addProvider(ModItemTagsProvider(output, baseLookupProvider))
		event.addProvider(ModGeneTagsProvider(output, lookupWithDatapack))
		event.addProvider(ModEntityTypeTagsProvider(output, baseLookupProvider))
		event.addProvider(ModEnchantmentTagsProvider(output, lookupWithDatapack))
		event.addProvider(ModPotionTagsProvider(output, baseLookupProvider))
		event.addProvider(ModDamageTypeTagsProvider(output, lookupWithDatapack))

		event.addProvider(ModLootTableProvider.create(output, baseLookupProvider))

		event.addProvider(ModGeneRequirementsProvider(output, baseLookupProvider))
		event.addProvider(ModEntityGenesProvider(output, baseLookupProvider))

		event.addProvider(
			AdvancementProvider(
				output,
				lookupWithDatapack,
				listOf(ModAdvancementSubProvider(lookupWithDatapack))
			)
		)
	}

}
