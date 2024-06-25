package dev.aaronhowser.mods.geneticsresequenced.datagen.tag

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.ModTags
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
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

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(ModTags.WOOLY_ITEM_TAG)
            .add(Items.SHEARS)
            .addTags(Tags.Items.TOOLS_SHEAR)

        //TODO: Add tooltip to items that have this tag, if you have the Gene, config to disable
        this.tag(ModTags.MAGNET_ITEM_BLACKLIST)
            .add(Items.COBBLESTONE)

        this.tag(ModTags.FIREBALL_ITEM_TAG)
            .add(Items.BLAZE_ROD)

        this.tag(ModTags.ENCHANTABLE_DELICATE_TOUCH)
            .add(ModItems.SCRAPER.get())

    }

}