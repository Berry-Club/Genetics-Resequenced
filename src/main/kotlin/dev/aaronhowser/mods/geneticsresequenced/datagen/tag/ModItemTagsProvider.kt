package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
    pOutput: PackOutput,
    pLookupProvider: CompletableFuture<HolderLookup.Provider>,
    pBlockTags: CompletableFuture<TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper?
) : ItemTagsProvider(pOutput, pLookupProvider, pBlockTags, GeneticsResequenced.ID, existingFileHelper) {

    companion object {
        val WOOLY_ITEM_TAG: TagKey<Item> = ItemTags.create(OtherUtil.modResource("wooly"))
        val FIREBALL_ITEM_TAG: TagKey<Item> = ItemTags.create(OtherUtil.modResource("fireball"))
        val MAGNET_ITEM_BLACKLIST: TagKey<Item> = ItemTags.create(OtherUtil.modResource("magnet_blacklist"))

        val ENCHANTABLE_DELICATE_TOUCH: TagKey<Item> =
            ItemTags.create(OtherUtil.modResource("enchantable/delicate_touch"))
    }

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(WOOLY_ITEM_TAG)
            .add(Items.SHEARS)
            .addTags(Tags.Items.TOOLS_SHEAR)

        this.tag(MAGNET_ITEM_BLACKLIST)
            .add(Items.COBBLESTONE)

        this.tag(FIREBALL_ITEM_TAG)
            .add(Items.BLAZE_ROD)

        this.tag(ENCHANTABLE_DELICATE_TOUCH)
            .add(ModItems.SCRAPER.get())

    }

}