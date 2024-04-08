package dev.aaronhowser.mods.geneticsresequenced

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item

object ModTags {

    val WOOLY_TAG: TagKey<Item> =
        TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation(GeneticsResequenced.ID, "wooly"))

    val FIREBALL_TAG: TagKey<Item> =
        TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation(GeneticsResequenced.ID, "fireball"))

    val MAGNET_BLACKLIST: TagKey<Item> =
        TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation(GeneticsResequenced.ID, "magnet_blacklist"))

    val SCRAPER_BLACKLIST: TagKey<EntityType<*>> =
        TagKey.create(Registry.ENTITY_TYPE_REGISTRY, ResourceLocation(GeneticsResequenced.ID, "scraper_blacklist"))

}