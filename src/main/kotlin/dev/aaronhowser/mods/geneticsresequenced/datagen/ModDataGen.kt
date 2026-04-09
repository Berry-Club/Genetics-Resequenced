package dev.aaronhowser.mods.geneticsresequenced.datagen

import com.klikli_dev.modonomicon.api.datagen.NeoBookProvider
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.gene.ModEntityGenesProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.gene.ModGeneRequirementsProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.loot.ModLootTableProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.model.ModBlockStateProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.model.ModItemModelProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.ModModonomiconProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.*
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.PackOutput
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(modid = GeneticsResequenced.MOD_ID)
object ModDataGen {

	@SubscribeEvent
	fun onGatherData(event: GatherDataEvent) {
		val generator: DataGenerator = event.generator
		val output: PackOutput = generator.packOutput
		val existingFileHelper: ExistingFileHelper = event.existingFileHelper
		val lookupProvider: CompletableFuture<HolderLookup.Provider> = event.lookupProvider

		val datapackRegistrySets = generator.addProvider(
			event.includeServer(),
			ModDatapackBuiltinEntriesProvider(output, lookupProvider)
		)

		val lookupWithDatapack: CompletableFuture<HolderLookup.Provider> = datapackRegistrySets.registryProvider

		generator.addProvider(
			event.includeClient(),
			ModItemModelProvider(output, existingFileHelper)
		)

		generator.addProvider(
			event.includeClient(),
			ModBlockStateProvider(output, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModRecipeProvider(output, lookupWithDatapack)
		)

		val blockTagProvider = generator.addProvider(
			event.includeServer(),
			ModBlockTagsProvider(output, lookupProvider, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModItemTagsProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModGeneTagsProvider(output, lookupWithDatapack, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModEnchantmentTagsProvider(output, lookupWithDatapack, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModPotionTagsProvider(output, lookupProvider, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModDamageTypeTagsProvider(output, lookupProvider, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			AdvancementProvider(
				output,
				lookupProvider,
				existingFileHelper,
				listOf(ModAdvancementSubProvider(lookupWithDatapack))
			)
		)

		generator.addProvider(
			event.includeServer(),
			ModLootTableProvider.create(output, lookupProvider)
		)

		val languageProvider = ModLanguageProvider(output)

		generator.addProvider(
			event.includeClient(),
			NeoBookProvider.of(
				event, lookupWithDatapack, ModModonomiconProvider(languageProvider::add)
			)
		)
		//Note by Klikli: There are two ways to integrate modonomicon with language providers.
		//                One is to register the language provider AFTER the book provider (as done here) which hopefully ensures that
		//                  the language provider is called after the book provider finishes, and allows the lang provider
		//                  to write both mod text and book text.
		//                The other is to use the AbstractModonomiconLanguageProvider for the mod texts together with a LanguageProviderCache
		generator.addProvider(event.includeClient(), languageProvider)

		generator.addProvider(
			event.includeServer(),
			ModGeneRequirementsProvider(output, lookupProvider, existingFileHelper)
		)

		generator.addProvider(
			event.includeServer(),
			ModEntityGenesProvider(output, lookupProvider, existingFileHelper)
		)

	}

}