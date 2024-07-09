package dev.aaronhowser.mods.geneticsresequenced.datagen.loot

import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags

class ModBlockLootTablesSubProvider(
    provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

    override fun generate() {
        for (block in ModBlocks.BLOCK_REGISTRY.entries) {
            dropSelf(block.get())
        }
    }

}