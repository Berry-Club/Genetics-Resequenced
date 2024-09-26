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
        private fun create(id: String): TagKey<Item> {
            return ItemTags.create(OtherUtil.modResource(id))
        }

        val SYRINGE: TagKey<Item> = create("syringe")
        val ACTIVATES_WOOLY: TagKey<Item> = create("wooly")
        val ACTIVATES_SHOOT_FIREBALL: TagKey<Item> = create("fireball")
        val MAGNET_ITEM_BLACKLIST: TagKey<Item> = create("magnet_blacklist")
        val ENCHANTABLE_DELICATE_TOUCH: TagKey<Item> = create("enchantable/delicate_touch")
        val PREVENTS_SOME_MOB_INTERACTION: TagKey<Item> = create("prevents_some_mob_interaction")
    }

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(SYRINGE)
            .add(
                ModItems.SYRINGE.get(),
                ModItems.METAL_SYRINGE.get()
            )

        this.tag(ACTIVATES_WOOLY)
            .add(Items.SHEARS)
            .addTags(Tags.Items.TOOLS_SHEAR)

        this.tag(MAGNET_ITEM_BLACKLIST)
            .add(Items.COBBLESTONE)

        this.tag(ACTIVATES_SHOOT_FIREBALL)
            .add(Items.BLAZE_ROD)

        this.tag(ENCHANTABLE_DELICATE_TOUCH)
            .add(ModItems.SCRAPER.get())

        this.tag(PREVENTS_SOME_MOB_INTERACTION)
            .add(
                ModItems.METAL_SYRINGE.get(),
                ModItems.SCRAPER.get()
            )
    }

}