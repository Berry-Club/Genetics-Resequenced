package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.datapack.ModDamageTypeProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.datapack.ModEnchantmentProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.gene.ModEntityGenesProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.gene.ModGeneProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.gene.ModGeneRequirementsProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.loot.ModLootTableProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.recipe.ModRecipeProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.*
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
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

		// NeoForge's legacy model generator classes were removed before 26.1.2.
		// Existing generated model assets stay in src/generated/resources until these providers are rewritten.
		event.addProvider(ModLanguageProvider(output))
	}

	@SubscribeEvent
	fun onGatherServerData(event: GatherDataEvent.Server) {
		val generator = event.generator
		val output = generator.packOutput
		val baseLookupProvider = event.lookupProvider

		event.createDatapackRegistryObjects(
			RegistrySetBuilder()
				.add(ModGenes.GENE_REGISTRY_KEY, ModGeneProvider::bootstrap)
				.add(Registries.DAMAGE_TYPE, ModDamageTypeProvider::bootstrap)
				.add(Registries.ENCHANTMENT, ModEnchantmentProvider::bootstrap)
		)

		val lookupWithDatapack = event.lookupProvider

		event.addProvider(ModRecipeProvider.Runner(output, lookupWithDatapack))

		val blockTagProvider = event.addProvider(ModBlockTagsProvider(output, baseLookupProvider))

		event.addProvider(ModItemTagsProvider(output, baseLookupProvider))

		event.addProvider(ModGeneTagsProvider(output, lookupWithDatapack))

		event.addProvider(ModEntityTypeTagsProvider(output, baseLookupProvider))

		event.addProvider(ModEnchantmentTagsProvider(output, lookupWithDatapack))

		event.addProvider(ModPotionTagsProvider(output, baseLookupProvider))

		event.addProvider(ModDamageTypeTagsProvider(output, lookupWithDatapack))

		event.addProvider(
			AdvancementProvider(
				output,
				lookupWithDatapack,
				listOf(ModAdvancementSubProvider(lookupWithDatapack))
			)
		)

		event.addProvider(ModLootTableProvider.create(output, baseLookupProvider))

		event.addProvider(ModGeneRequirementsProvider(output, baseLookupProvider))

		event.addProvider(ModEntityGenesProvider(output, baseLookupProvider))

	}

}
