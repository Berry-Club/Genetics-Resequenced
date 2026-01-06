package dev.aaronhowser.mods.geneticsresequenced.datagen.loot

import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets

object ModLootTableProvider {

	fun create(output: PackOutput): LootTableProvider {
		return LootTableProvider(
			output,
			setOf(),
			listOf(
				LootTableProvider.SubProviderEntry(
					::ModBlockLootTablesSubProvider,
					LootContextParamSets.BLOCK
				)
			)
		)
	}
}