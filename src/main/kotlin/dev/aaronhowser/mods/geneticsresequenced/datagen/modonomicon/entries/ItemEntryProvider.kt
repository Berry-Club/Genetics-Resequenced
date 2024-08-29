package dev.aaronhowser.mods.geneticsresequenced.datagen.modonomicon.entries

import com.klikli_dev.modonomicon.api.datagen.CategoryProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import net.minecraft.world.level.ItemLike

abstract class ItemEntryProvider(
    parent: CategoryProviderBase?,
    val itemLike: ItemLike
) : BaseEntryProvider(parent, itemLike.asItem().descriptionId) {

    override fun entryIcon(): BookIconModel {
        return BookIconModel.create(itemLike)
    }
}