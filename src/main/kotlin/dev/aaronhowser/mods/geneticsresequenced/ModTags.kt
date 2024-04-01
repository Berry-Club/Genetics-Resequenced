package dev.aaronhowser.mods.geneticsresequenced

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object ModTags {

    val SHEARS_TAG_RL: ResourceLocation = ResourceLocation(GeneticsResequenced.ID, "wooly")
    val SHEARS_TAG: TagKey<Item> = TagKey.create(Registry.ITEM_REGISTRY, SHEARS_TAG_RL)

}