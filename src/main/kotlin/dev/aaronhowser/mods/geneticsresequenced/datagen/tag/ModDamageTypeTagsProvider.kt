package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.DamageTypeTagsProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModDamageTypeTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : DamageTypeTagsProvider(output, lookupProvider, GeneticsResequenced.ID, existingFileHelper) {

    companion object {
        private fun createType(name: String): ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, OtherUtil.modResource(name))

        val STEP_ON_SYRINGE: ResourceKey<DamageType> = createType("step_on_syringe")
        val USE_SYRINGE: ResourceKey<DamageType> = createType("use_syringe")
        val USE_SCRAPER: ResourceKey<DamageType> = createType("use_scraper")
    }

    override fun addTags(provider: HolderLookup.Provider) {

        this.tag(DamageTypeTags.NO_IMPACT)
            .add(
                STEP_ON_SYRINGE,
                USE_SYRINGE,
                USE_SCRAPER
            )

        this.tag(DamageTypeTags.NO_ANGER)
            .add(
                STEP_ON_SYRINGE,
                USE_SYRINGE,
                USE_SCRAPER
            )

        this.tag(DamageTypeTags.NO_KNOCKBACK)
            .add(
                STEP_ON_SYRINGE,
                USE_SYRINGE,
                USE_SCRAPER
            )

    }

}