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

        val SYRINGES: TagKey<Item> = create("syringes")
        val ACTIVATES_SHOOT_FIREBALL_GENE: TagKey<Item> = create("activates_shoot_fireball_gene")
        val MAGNET_ITEM_BLACKLIST: TagKey<Item> = create("item_magnet_gene_blacklist")
        val ENCHANTABLE_DELICATE_TOUCH: TagKey<Item> = create("enchantable/delicate_touch")
        val PREVENTS_SOME_MOB_INTERACTION: TagKey<Item> = create("prevents_some_mob_interaction")
    }

    override fun addTags(pProvider: HolderLookup.Provider) {

        this.tag(SYRINGES)
            .add(
                ModItems.SYRINGE.get(),
                ModItems.METAL_SYRINGE.get()
            )

        this.tag(MAGNET_ITEM_BLACKLIST)
            .add(Items.COBBLESTONE)

        this.tag(ACTIVATES_SHOOT_FIREBALL_GENE)
            .add(Items.BLAZE_ROD)

        this.tag(ENCHANTABLE_DELICATE_TOUCH)
            .add(ModItems.SCRAPER.get())

        this.tag(PREVENTS_SOME_MOB_INTERACTION)
            .add(
                ModItems.METAL_SYRINGE.get(),
                ModItems.SCRAPER.get(),
                ModItems.GENE_CHECKER.get()
            )
    }

}