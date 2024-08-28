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
        private fun create(id: String): TagKey<EntityType<*>> {
            return TagKey.create(Registries.ENTITY_TYPE, OtherUtil.modResource(id))
        }

        val SCRAPER_ENTITY_BLACKLIST = create("scraper_blacklist")
        val AVOIDS_SCARE_CREEPER_GENE = create("avoids_scare_creeper_gene")
        val AVOIDS_SCARE_ZOMBIE_GENE = create("avoids_scare_zombie_gene")
        val AVOIDS_SCARE_SKELETON_GENE = create("avoids_scare_skeleton_gene")
        val AVOIDS_SCARE_SPIDER_GENE = create("avoids_scare_spider_gene")
    }

    override fun addTags(pProvider: HolderLookup.Provider) {
        this.tag(SCRAPER_ENTITY_BLACKLIST)
            .add(EntityType.ARMOR_STAND, EntityType.PAINTING)

        this.tag(AVOIDS_SCARE_CREEPER_GENE)
            .add(EntityType.CREEPER)

        this.tag(AVOIDS_SCARE_ZOMBIE_GENE)
            .add(
                EntityType.ZOMBIE,
                EntityType.DROWNED,
                EntityType.HUSK,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.ZOMBIE_HORSE
            )

        this.tag(AVOIDS_SCARE_SKELETON_GENE)
            .add(
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.BOGGED,
                EntityType.WITHER_SKELETON,
                EntityType.SKELETON_HORSE
            )

        this.tag(AVOIDS_SCARE_SPIDER_GENE)
            .add(
                EntityType.SPIDER,
                EntityType.CAVE_SPIDER
            )
    }

}