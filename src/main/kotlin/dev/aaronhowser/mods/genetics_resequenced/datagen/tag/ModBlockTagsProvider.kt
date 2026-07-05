package dev.aaronhowser.mods.genetics_resequenced.datagen.tag

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import java.util.concurrent.CompletableFuture

class ModBlockTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : BlockTagsProvider(output, lookupProvider, GeneticsResequenced.MOD_ID) {

	override fun addTags(pProvider: HolderLookup.Provider) {
		this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.add(*ModBlocks.BLOCK_REGISTRY.entries.map { it.get() }.toTypedArray())

		this.tag(BlockTags.AIR)
			.add(ModBlocks.BIOLUMINESCENCE_BLOCK.get())
	}

}
