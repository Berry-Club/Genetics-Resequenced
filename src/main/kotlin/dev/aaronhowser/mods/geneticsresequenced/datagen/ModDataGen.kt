package dev.aaronhowser.mods.geneticsresequenced.datagen

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
import net.minecraft.data.advancements.AdvancementProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.util.concurrent.CompletableFuture

@Mod.EventBusSubscriber(modid = GeneticsResequenced.MOD_ID)
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

		val lookupWithGenes: CompletableFuture<HolderLookup.Provider> = datapackRegistrySets.registryProvider

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
			ModRecipeProvider(output, lookupWithGenes)
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
			ModGeneTagsProvider(output, lookupWithGenes, existingFileHelper)
		)
		generator.addProvider(
			event.includeServer(),
			ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper)
		)
		generator.addProvider(
			event.includeServer(),
			ModEnchantmentTagsProvider(output, lookupProvider, existingFileHelper)
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
				listOf(ModAdvancementSubProvider(lookupWithGenes))
			)
		)

		generator.addProvider(
			event.includeServer(),
			ModLootTableProvider.create(output, lookupProvider)
		)

		val languageProvider = ModLanguageProvider(output)
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