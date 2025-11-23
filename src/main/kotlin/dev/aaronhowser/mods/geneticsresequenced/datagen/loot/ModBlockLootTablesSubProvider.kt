package dev.aaronhowser.mods.geneticsresequenced.datagen.loot

import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredHolder

class ModBlockLootTablesSubProvider(
	provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

	override fun generate() {
		val dropSelf = listOf(
			ModBlocks.ANTI_FIELD_BLOCK.get(),
			ModBlocks.COAL_GENERATOR.get(),
			ModBlocks.CELL_ANALYZER.get(),
			ModBlocks.DNA_EXTRACTOR.get(),
			ModBlocks.DNA_DECRYPTOR.get(),
			ModBlocks.BLOOD_PURIFIER.get(),
			ModBlocks.PLASMID_INFUSER.get(),
			ModBlocks.PLASMID_INJECTOR.get(),
			ModBlocks.INCUBATOR.get(),
			ModBlocks.ADVANCED_INCUBATOR.get()
		)

		for (block in dropSelf) {
			dropSelf(block)
		}

		val noDrop = listOf(
			ModBlocks.WEB_DEFENSE_BLOCK.get(),
		)

		for (block in noDrop) {
			add(block, noDrop())
		}
	}

	override fun getKnownBlocks(): List<Block> {
		return ModBlocks.BLOCK_REGISTRY
			.entries
			.map(DeferredHolder<Block, out Block>::get)
	}

}