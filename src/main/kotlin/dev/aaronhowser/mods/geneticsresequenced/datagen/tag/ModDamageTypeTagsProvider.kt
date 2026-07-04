package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.datapack.ModDamageTypeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.DamageTypeTagsProvider
import net.minecraft.tags.DamageTypeTags
import java.util.concurrent.CompletableFuture

class ModDamageTypeTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : DamageTypeTagsProvider(output, lookupProvider, GeneticsResequenced.MOD_ID) {

	override fun addTags(provider: HolderLookup.Provider) {
		this.tag(DamageTypeTags.NO_IMPACT)
			.add(
				ModDamageTypeProvider.STEP_ON_SYRINGE,
				ModDamageTypeProvider.USE_SYRINGE,
				ModDamageTypeProvider.USE_SCRAPER
			)

		this.tag(DamageTypeTags.NO_ANGER)
			.add(
				ModDamageTypeProvider.STEP_ON_SYRINGE,
				ModDamageTypeProvider.USE_SYRINGE,
				ModDamageTypeProvider.USE_SCRAPER
			)

		this.tag(DamageTypeTags.NO_KNOCKBACK)
			.add(
				ModDamageTypeProvider.STEP_ON_SYRINGE,
				ModDamageTypeProvider.USE_SYRINGE,
				ModDamageTypeProvider.USE_SCRAPER
			)
	}

}
