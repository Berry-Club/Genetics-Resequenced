package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModDamageTypeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.DamageTypeTagsProvider
import net.minecraft.tags.DamageTypeTags
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModDamageTypeTagsProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : DamageTypeTagsProvider(output, lookupProvider, GeneticsResequenced.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {

		this.tag(DamageTypeTags.NO_IMPACT)
			.add(
				ModDamageTypeProvider.STEP_ON_SYRINGE,
				ModDamageTypeProvider.USE_SYRINGE,
				ModDamageTypeProvider.USE_SCRAPER,
				ModDamageTypeProvider.VIRUS
			)

		this.tag(DamageTypeTags.NO_ANGER)
			.add(
				ModDamageTypeProvider.STEP_ON_SYRINGE,
				ModDamageTypeProvider.USE_SYRINGE,
				ModDamageTypeProvider.USE_SCRAPER,
				ModDamageTypeProvider.VIRUS
			)

		//TODO
//		this.tag(DamageTypeTags.NO_KNOCKBACK)
//			.add(
//				STEP_ON_SYRINGE,
//				USE_SYRINGE,
//				USE_SCRAPER
//			)

	}

}