package dev.aaronhowser.mods.geneticsresequenced

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

@Suppress("MemberVisibilityCanBePrivate")
object ModTags {

    val WOOLY_TAG_RL = ResourceLocation(GeneticsResequenced.ID, "wooly")
    val WOOLY_TAG: TagKey<Item> = TagKey.create(Registry.ITEM_REGISTRY, WOOLY_TAG_RL)

    val FIREBALL_TAG_RL = ResourceLocation(GeneticsResequenced.ID, "fireball")
    val FIREBALL_TAG: TagKey<Item> = TagKey.create(Registry.ITEM_REGISTRY, FIREBALL_TAG_RL)

    val MAGNET_BLACKLIST_RL = ResourceLocation(GeneticsResequenced.ID, "magnet_blacklist")
    val MAGNET_BLACKLIST: TagKey<Item> = TagKey.create(Registry.ITEM_REGISTRY, MAGNET_BLACKLIST_RL)

}