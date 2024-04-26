package dev.aaronhowser.mods.geneticsresequenced

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Registry
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item

object ModTags {

    val WOOLY_ITEM_TAG: TagKey<Item> =
        TagKey.create(Registry.ITEM_REGISTRY, OtherUtil.modResource("wooly"))

    val FIREBALL_ITEM_TAG: TagKey<Item> =
        TagKey.create(Registry.ITEM_REGISTRY, OtherUtil.modResource("fireball"))

    val MAGNET_ITEM_BLACKLIST: TagKey<Item> =
        TagKey.create(Registry.ITEM_REGISTRY, OtherUtil.modResource("magnet_blacklist"))

    val SCRAPER_ENTITY_BLACKLIST: TagKey<EntityType<*>> =
        TagKey.create(Registry.ENTITY_TYPE_REGISTRY, OtherUtil.modResource("scraper_blacklist"))

}