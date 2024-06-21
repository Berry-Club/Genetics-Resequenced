package dev.aaronhowser.mods.geneticsresequenced

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item

object ModTags {

    val WOOLY_ITEM_TAG: TagKey<Item> = ItemTags.create(OtherUtil.modResource("wooly"))
    val FIREBALL_ITEM_TAG: TagKey<Item> = ItemTags.create(OtherUtil.modResource("fireball"))
    val MAGNET_ITEM_BLACKLIST: TagKey<Item> = ItemTags.create(OtherUtil.modResource("magnet_blacklist"))

    val ENCHANTABLE_DELICATE_TOUCH_ITEM_TAG: TagKey<Item> = ItemTags.create(OtherUtil.modResource("enchantable/delicate_touch"))

    val SCRAPER_ENTITY_BLACKLIST: TagKey<EntityType<*>> = TagKey.create(
        Registries.ENTITY_TYPE,
        OtherUtil.modResource("scraper_blacklist")
    )

}