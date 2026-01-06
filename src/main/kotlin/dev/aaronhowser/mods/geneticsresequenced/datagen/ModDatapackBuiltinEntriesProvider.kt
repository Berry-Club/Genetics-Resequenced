package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.gene.ModGeneProvider
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.PackOutput
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider
import java.util.concurrent.CompletableFuture

class ModDatapackBuiltinEntriesProvider(
	output: PackOutput,
	registries: CompletableFuture<HolderLookup.Provider>
) : DatapackBuiltinEntriesProvider(
	output,
	registries,
	BUILDER,
	setOf(GeneticsResequenced.MOD_ID)
) {

	companion object {
		val BUILDER: RegistrySetBuilder = RegistrySetBuilder()
			.add(ModGenes.GENE_REGISTRY_KEY, ModGeneProvider::bootstrap)
	}

}