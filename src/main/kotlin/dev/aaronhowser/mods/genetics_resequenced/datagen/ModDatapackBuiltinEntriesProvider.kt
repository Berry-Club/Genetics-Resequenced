package dev.aaronhowser.mods.genetics_resequenced.datagen

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.gene.ModGeneProvider
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
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
			.add(Registries.ENCHANTMENT, ModEnchantmentProvider::bootstrap)
	}

}