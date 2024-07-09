package dev.aaronhowser.mods.geneticsresequenced.datagen.loot

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture

object ModLootTableProvider {

    fun create(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>): LootTableProvider {
        return LootTableProvider(
            output,
            setOf(),
            listOf(
                LootTableProvider.SubProviderEntry(
                    ::ModBlockLootTablesSubProvider,
                    LootContextParamSets.BLOCK
                )
            ),
            lookupProvider
        )
    }
}