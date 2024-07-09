package dev.aaronhowser.mods.geneticsresequenced.datagen.loot

import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block

class ModBlockLootTablesSubProvider(
    provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

    override fun generate() {
        for (block in knownBlocks) {
            dropSelf(block)
        }
    }

    override fun getKnownBlocks(): List<Block> {
        return ModBlocks.BLOCK_REGISTRY.entries.map { it.get() } - ModBlocks.BIOLUMINESCENCE_BLOCK.get()
    }

}