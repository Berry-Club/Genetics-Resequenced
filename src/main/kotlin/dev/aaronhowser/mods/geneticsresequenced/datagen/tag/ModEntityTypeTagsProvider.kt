package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.EntityTypeTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModEntityTypeTagsProvider(
    pOutput: PackOutput,
    pProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper?
) : EntityTypeTagsProvider(pOutput, pProvider, GeneticsResequenced.ID, existingFileHelper) {

    companion object {
        val SCRAPER_ENTITY_BLACKLIST: TagKey<EntityType<*>> = TagKey.create(
            Registries.ENTITY_TYPE,
            OtherUtil.modResource("scraper_blacklist")
        )
    }

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(SCRAPER_ENTITY_BLACKLIST)
            .add(EntityType.ARMOR_STAND, EntityType.PAINTING)

    }

}