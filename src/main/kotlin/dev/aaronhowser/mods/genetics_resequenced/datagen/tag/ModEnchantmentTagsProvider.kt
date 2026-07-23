package dev.aaronhowser.mods.genetics_resequenced.datagen.tag

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.datapack.ModEnchantmentProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EnchantmentTagsProvider
import net.minecraft.tags.EnchantmentTags
import java.util.concurrent.CompletableFuture

class ModEnchantmentTagsProvider(
	pOutput: PackOutput,
	pProvider: CompletableFuture<HolderLookup.Provider>
) : EnchantmentTagsProvider(pOutput, pProvider, GeneticsResequenced.MOD_ID) {

	override fun addTags(p0: HolderLookup.Provider) {
		this.tag(EnchantmentTags.NON_TREASURE)
			.add(
				ModEnchantmentProvider.DELICATE_TOUCH,
				ModEnchantmentProvider.SURGICAL_PRECISION
			)
	}


}
